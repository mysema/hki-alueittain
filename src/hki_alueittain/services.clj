(ns hki-alueittain.services
  (:require [clojure.java.io :as jio]
            [clojure.set :as set]
            [clojure.string :as str]
            [dk.ative.docjure.spreadsheet :refer :all]
            [clj-yaml.core :as yaml]
            [hki-alueittain.model :as model]))

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
  (jio/copy (:tempfile file) (jio/file (str data-path "/" (:filename file))))
  "")

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
  (first (filter (fn [row] (= (first row) (str area))) (:rows @model/data))))

(defn data-for-ui
  [row headers ui-config]
  (clojure.pprint/pprint row)
  (clojure.pprint/pprint headers)
  (clojure.pprint/pprint ui-config)
  (for [[label content] ui-config]
    [(name label) 
     (for [[label {:keys [columns rows]}] content]
       [(name label) {:headers (map :label columns)
                      :rows (for [{:keys [label source-columns]} rows]
                              (cons label (for [column source-columns]
                                            (let [formatter (column-formatter column)]
                                            (formatter (nth row (.indexOf headers (keyword column))))))))}])]))

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
    (reset! model/data excel-data)
    (reset! model/statistics-config ui-config))
  "")

(comment
  (->> (load-workbook "spreadsheet.xlsx")
       (select-sheet "Price List")
       (select-columns {:A :name, :B :price})))
