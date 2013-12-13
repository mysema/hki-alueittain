(ns hki-alueittain.services
  (:require  [clojure.java.io :as jio]
             [clojure.set :as set]
             [clojure.string :refer (split split-lines)]
             [dk.ative.docjure.spreadsheet :refer :all]
             [clj-yaml.core :as yaml]))

(def ^:dynamic data-path "data")

(def data (atom {}))

(def areas
  (->> (slurp (jio/resource "helsinki-areas.csv") :encoding "UTF-8")
       split-lines
       (map #(split % #","))
       (into {})))

(defn upload-file!
  [file]
  (jio/copy (:tempfile file) (jio/file (str data-path "/" (:filename file))))
  "")

(defn get-config
  [path]
  (let [path (str data-path "/" path)]
    (-> (slurp path :encoding "UTF-8")
        yaml/parse-string)))

(defn data-for-ui
  [row headers ui-config]
  (for [[label content] ui-config]
    [(name label) 
     (for [[label {:keys [columns rows]}] content]
       [(name label) {:headers (map :label columns)
                      :rows (for [{:keys [label source-columns]} rows]
                              (cons label (for [column source-columns]
                                            (nth row (.indexOf headers (keyword column))))))}])]))

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
    (reset! data excel-data))
  "")

(comment
  (->> (load-workbook "spreadsheet.xlsx")
       (select-sheet "Price List")
       (select-columns {:A :name, :B :price})))
