(ns hki-alueittain.views
  (:require [hiccup.core :refer (html)]))

(defn as-table
  [{:keys [headers rows]}]
  (html
    [:table 
     [:thead 
      [:tr (map (fn [header] [:th header])
                headers)]]
     [:tbody (map (fn [row]
                    [:tr (map (fn [cell] [:td cell])
                              row)])
                  rows)]]))