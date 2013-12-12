(ns hki-alueittain.services
  (:require  [clojure.java.io :as jio]
             [clojure.set :as set]
             [clojure.string :refer (split split-lines)]
             [dk.ative.docjure.spreadsheet :refer :all]
             [clj-yaml.core :as yaml]))

(def areas
  (->> (slurp (jio/resource "helsinki-areas.csv") :encoding "UTF-8")
       split-lines
       (map #(split % #","))
       (into {})))
  
(defn- get-excel-config
  [path]
  (-> (slurp path :encoding "UTF-8")
      yaml/parse-string))

(defn get-excel-data
  [path]
  (let [[x & xs] (-> (load-workbook path)
                     (.getSheetAt 0)
                     row-seq)
        headers (map read-cell x)
        rows (map (partial map read-cell) xs)
        config-path (str (subs path 0 (- (.length path) 5)) "-config.yaml")
        config (get-excel-config config-path)]
    {:headers (map (set/map-invert (:columns config)) headers)
     :rows rows}))
  
(comment
  (->> (load-workbook "spreadsheet.xlsx")
       (select-sheet "Price List")
       (select-columns {:A :name, :B :price})))