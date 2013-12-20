(ns hki-alueittain.services
  (:require [clojure.java.io :as jio]
            [clojure.set :as set]
            [clojure.string :as str]
            [dk.ative.docjure.spreadsheet :refer :all]
            [clj-yaml.core :as yaml]))

(defonce data (atom {})) 

(defonce statistics-config (atom {}))

(def ^:dynamic data-path "data")

(def areas
  (->> (slurp (jio/resource "helsinki-areas.csv") :encoding "UTF-8")
       str/split-lines
       (map #(str/split % #","))
       (into {})))

(def basic-areas
  (reduce (fn [acc [code name]] 
            (if (> (Integer/valueOf code) 100) 
              (assoc acc code name) 
              acc)) 
          {} areas))

(defn upload-file!
  [file]
  (jio/copy (:tempfile file) (jio/file (str data-path "/" (:filename file)))))

(defn data-published?
  []
  (not (empty? @data)))

(defn get-config
  [path]
  (let [path (str data-path "/" path)]
    (-> (slurp path :encoding "UTF-8")
        yaml/parse-string)))

(defn column-formatter
  [column]
  (if (.endsWith (name column) "rel")
    (fn [data] 
      (str (str/replace data #"\." ",") " %")) 
    (fn [data]
      (str/replace (str/replace data #"\..*$" "") #"(.+)(.{3})$" "$1 $2"))))

(defn data-for-area
  [area]
  (first (filter (fn [row] (= (first row) (str area))) (:rows @data))))

(defn city-average-for-source-column
  [source-column]
  (if (data-published?)
    (let [i (.indexOf (:headers @data) (keyword source-column))
          avg (nth (first (:rows @data)) i)
          formatter (column-formatter source-column)]
      (formatter avg))
    "?"))

(defn with-city-averages
  [columns rows]
  [(concat columns [{:label "Helsingin keskiarvo" :name "city-average"}])
   (map (fn [row] (assoc row :city-average (city-average-for-source-column (last (:source-columns row))))) rows)])

(defn data-for-ui
  [row headers ui-config]
  (for [[label content] ui-config]
    [(name label) 
     (for [[label {:keys [columns rows show-city-average]}] content]
       (let [[columns rows] (if 
                              show-city-average 
                              (with-city-averages columns rows) 
                              [columns rows])
             formatted-rows (for [{:keys [label source-columns city-average]} rows]
                              (let [area-row (cons label (for [column source-columns]
                                                           (let [formatter (column-formatter column)]
                                                             (formatter (nth row (.indexOf headers (keyword column)))))))]
                                (if city-average
                                  (concat area-row [city-average])
                                  area-row)))]
         [(name label) {:headers (map :label columns)
                        :rows formatted-rows}]))]))

(defn maybe-area-statistics
  [area]
  (when (and (data-published?) area (pos? (Integer/valueOf area)))
    (data-for-ui (data-for-area area) (:headers @data) @statistics-config)))

(defn get-excel-data
  [mapping excel-path]
  (let [[x & xs] (-> (load-workbook excel-path)
                     (.getSheetAt 0)
                     row-seq)
        headers (map read-cell x)
        rows (map (partial map read-cell) xs)]
    {:headers (map (set/map-invert (:columns mapping)) headers)
     :rows rows}))
  
(defn publish!
  [mapping-filename ui-config-filename excel-filename]
  (let [mapping (get-config mapping-filename) 
        ui-config (get-config ui-config-filename)
        excel-data (get-excel-data mapping (str data-path "/" excel-filename))]
    (reset! data excel-data)
    (reset! statistics-config ui-config))
  "")

(comment
  (->> (load-workbook "spreadsheet.xlsx")
       (select-sheet "Price List")
       (select-columns {:A :name, :B :price})))
