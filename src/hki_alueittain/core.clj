(ns hki-alueittain.core
  (:require [ring.util.response :refer (redirect)]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [hki-alueittain.services :as services]
            [hki-alueittain.views :as views]))

(defroutes routes
  (GET "/" []
    (views/areas-page))
  
  (GET "/admin" []
    (views/admin-page))

  (GET "/data/:id" [id]
    (-> (services/get-excel-data id)
         views/excel-page))    
  
  (route/resources "/")
  
  (route/not-found "Page not found"))

(def app (handler/site routes))

(comment
  (use 'ring.server.standalone)
  (def handler (handler/site #'routes))
  (defonce server (serve #'handler {:auto-reload? false
                                    :open-browser? false}))
  
  )
