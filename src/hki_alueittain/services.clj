(ns hki-alueittain.services
  (:require  [clojure.java.io :as jio]
             [clojure.set :as set]
             [clojure.string :refer (split split-lines)]
             [dk.ative.docjure.spreadsheet :refer :all]
             [clj-yaml.core :as yaml]))

(def data-path "data")

(def areas
  (->> (slurp (jio/resource "helsinki-areas.csv") :encoding "UTF-8")
       split-lines
       (map #(split % #","))
       (into {})))

(defn upload-file
  [file]
  (jio/copy (:tempfile file) (jio/file (str data-path "/" (:filename file))))
  "File uploaded successfully")
  
(defn get-excel-config
  [path]
  (let [path (str data-path "/" path "-config.yaml")]
    (-> (slurp path :encoding "UTF-8")
        yaml/parse-string)))

(defn get-ui-config
  [path]
  (let [path (str data-path "/" path "-ui-config.yaml")]
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
  [path]
  (let [xlsx-path (str data-path "/" path ".xlsx")
        [x & xs] (-> (load-workbook xlsx-path)
                     (.getSheetAt 0)
                     row-seq)
        headers (map read-cell x)
        rows (map (partial map read-cell) xs)        
        config (get-excel-config path)]
    {:headers (map (set/map-invert (:columns config)) headers)
     :rows rows}))
  
(comment
  (->> (load-workbook "spreadsheet.xlsx")
       (select-sheet "Price List")
       (select-columns {:A :name, :B :price})))
