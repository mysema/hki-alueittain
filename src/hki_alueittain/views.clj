(ns hki-alueittain.views
  (:require [hiccup.core :refer (html)]))

(defn- tags
  [tag]
  (fn [& attrs]
    (map (partial vector tag) attrs)))

(def meta-tags
  (tags :meta))

(def link-tags
  (tags :link))

(def nav
  (array-map :areas
             {:name "Alueet" :href "/"}
             :admin
             {:name "Ylläpito" :href "/admin"}
             :map 
             {:name "Karttavertailu" :href "/map.html"}))

(defn nav-with-active
  [nav active]
  (if (nil? active)
    nav
    (assoc-in nav [active :active] true)))

(defn navbar
  [links]
  [:nav.navbar.navbar-default.navbar-fixed-top
   {:role "navigation"}
   [:div.navbar-header
    [:a.navbar-brand {:href "#"} "helsinki alueittain"]]
   [:div#nav-collapse.collapse.navbar-collapse
    [:ul.nav.navbar-nav
     (for [[_ link] links]
       [:li 
        (when (:active link) {:class "active"}) 
        [:a {:href (:href link)} (:name link)]])]]])

(defn layout
  [& {:keys [title active content]}]
  (html 
    [:html
     [:head
      [:title title]]
      (meta-tags {:charset "utf-8"}
                 {:http-equiv "x-ua-compatible" :content "ie=edge,chrome=1"}
                 {:name "description" :content ""}
                 {:name "viewport" :content "width=device-width"})
      (link-tags {:rel "stylesheet" :href "/styles/styles.css"})
     [:body 
      (navbar (nav-with-active nav active))
      [:section 
       {:role "main"}
       content]]]))

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

(defn admin-page
  []
  (layout :title "Admin page"
          :active :admin  
          :content "Foobar"))
