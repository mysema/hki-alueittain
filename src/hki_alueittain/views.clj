(ns hki-alueittain.views
  (:require [hiccup.core :refer (html)]
            [hiccup.page :refer (include-js)]
            [hki-alueittain.services :as services]))

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
             {:name "Ylläpito" :href "/admin"}))

(defn- nav-with-active
  [nav active]
  (if (nil? active)
    nav
    (assoc-in nav [active :active] true)))

(defn- navbar
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

(defn- layout
  [& {:keys [title active content]}]
  (html 
    [:html
     [:head
      [:title title]
      (meta-tags {:charset "utf-8"}
                 {:http-equiv "x-ua-compatible" :content "ie=edge,chrome=1"}
                 {:name "description" :content ""}
                 {:name "viewport" :content "width=device-width"})
      (link-tags {:rel "stylesheet" :href "/styles/styles.css"})
      (include-js "scripts/vendor/jquery.js")
      (include-js "scripts/vendor/jquery.ui.widget.js")
      (include-js "scripts/vendor/jquery.iframe-transport.js")
      (include-js "scripts/vendor/jquery.fileupload.js")
      (include-js "scripts/app.js")]
     [:body 
      (navbar (nav-with-active nav active))
      [:section 
       {:role "main"}
       content]]]))

(defn areas-page
  [data-published? area statistics]
  (if (not data-published?)
    (layout :title "Alueet"
            :active :areas
            :content "Ei tietoja vielä.")  
    (layout :title "Alueet"
            :active :areas
            :content [:div.col-md-6
                      [:form.form-horizontal
                       {:role "form"}
                       [:div.form-group
                        [:label.col-md-3.control-label "Valitse alue:"]
                        [:div.col-md-9
                         [:select#area.form-control
                          {:name "area"}
                          [:option {:value 0} "Ei valittu"]
                          (for [[code name] services/basic-areas]
                            [:option (if (= area code) {:value code :selected "selected"} {:value code}) name])]]]]
                      (for [[header group] statistics]
                        [:div
                         [:h3 header]
                         (for [[table-title table-data] group]
                           [:table.table
                            [:thead
                             [:tr
                              [:th table-title]
                              (for [header (:headers table-data)]
                                [:th header])]]
                            [:tbody
                             (for [row (:rows table-data)]
                               [:tr (map (partial vector :td) row)])]])])])))

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
