(ns hki-alueittain.views
  (:require [hiccup.core :refer (html)]))

(defn layout
  [& {:keys [title content]}]
  (html 
    [:html
     [:head
      [:title title]]
     [:body content]]))

(defn- as-table
  [{:keys [headers rows]}]
  [:table 
   [:thead 
    [:tr (map (fn [header] [:th header])
              headers)]]
   [:tbody (map (fn [row]
                  [:tr (map (fn [cell] [:td cell])
                            row)])
                rows)]])

(defn excel-page
  [content]
  (layout :title "Excel example"
          :content (as-table content)))