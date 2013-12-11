(ns hki-alueittain.services
  (:require [dk.ative.docjure.spreadsheet :refer :all]))

(defn get-data
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