(ns hki-alueittain.core
  (:require [ring.util.response :refer (redirect)]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [hki-alueittain.services :as services]
            [hki-alueittain.views :as views]))

(defroutes routes
  (GET "/" []
    (redirect "index.html"))
  
  (GET "/excel" []
    (-> "attachments/prototype/tblDataTiedot.xlsx"
         services/get-excel-data
         views/excel-page))    
  
  (route/resources "/")
  
  (route/not-found "Page not found"))

(def app (handler/site routes))

(comment
  (use 'ring.server.standalone)
  (def handler (handler/site #'routes))
  (defonce server (serve #'handler {:auto-reload? false
                                    :open-browser? false})))
