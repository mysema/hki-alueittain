(ns hki-alueittain.services
  (:require  [clojure.java.io :as jio]
             [clojure.string :refer (split split-lines)]
             [dk.ative.docjure.spreadsheet :refer :all]))

(def areas
  (->> (slurp (jio/resource "helsinki-areas.csv") :encoding "UTF-8")
       split-lines
       (map #(split % #","))
       (into {})))
  
(defn get-excel-data
  [id]
  (let [[x & xs] (-> (load-workbook id)
                     (.getSheetAt 0)
                     row-seq)
        headers (map read-cell x)
        rows (map (partial map read-cell) xs)]
    {:headers headers
     :rows rows}))
  
(comment
  (->> (load-workbook "spreadsheet.xlsx")
       (select-sheet "Price List")
       (select-columns {:A :name, :B :price})))