(ns hki-alueittain.views
  (:require [hiccup.core :refer (html)]
            [hiccup.page :refer (include-js)]))

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
      (include-js "scripts/vendor/jquery.js")
      (include-js "scripts/vendor/jquery.ui.widget.js")
      (include-js "scripts/vendor/jquery.iframe-transport.js")
      (include-js "scripts/vendor/jquery.fileupload.js")
      (include-js "scripts/upload.js")

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

(defn areas-page
  []
  (layout :title "Alueet"
          :active :areas
          :content "Insert wild statistics here"))

(defn admin-page
  []
  (layout :title "Ylläpito"
          :active :admin  
          :content [:form.files-form.form-horizontal
                    {:role "form" :method "post" :enctype "multipart/form-data"}
                    [:input#mapping-upload {:type "file" :name "file" :data-url "/admin"}]
                    [:input#ui-config-upload {:type "file" :name "file" :data-url "/admin"}]
                    [:input#excel-upload {:type "file" :name "file" :data-url "/admin"}]
                    [:div.form-group
                     [:label.col-sm-2.control-label
                      "Lataa tietomalli:"]
                     [:div.col-sm-2 [:input#mapping-input.form-control {:type "text"}]]
                     [:button#mapping.btn.btn-default "Selaa"]]
                    [:div.form-group
                     [:label.col-sm-2.control-label
                      "Lataa konfigurointi:"]
                     [:div.col-sm-2 [:input#ui-config-input.form-control {:type "text"}]]
                     [:button#ui-config.btn.btn-default "Selaa"]]
                    [:div.form-group
                     [:label.col-sm-2.control-label
                      "Lataa Exceltiedosto:"]
                     [:div.col-sm-2 [:input#excel-input.form-control {:type "text"}]]
                     [:button#excel.btn.btn-default "Selaa"]]
                    [:div.form-group
                     [:div.col-sm-offset-2.col-sm-10
                      [:button#publish.btn.btn-primary.btn-lg {:type "submit"} "Julkaise"]]]]))
