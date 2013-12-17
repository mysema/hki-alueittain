(ns hki-alueittain.core
  (:require [ring.util.response :refer (redirect)]
            [ring.middleware.multipart-params :as mp]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [hki-alueittain.services :as services]
            [hki-alueittain.views :as views]))

(defroutes routes
  (GET "/" [area]
   (if (services/data-published) 
      (let [area (or area "0")
            statistics (services/maybe-area-statistics area)]
        (views/areas-page area statistics))
      (views/areas-page-no-statistics)))

  (GET "/admin" []
    (views/admin-page))

  (mp/wrap-multipart-params
    (POST "/admin" [file] 
      (services/upload-file! file)))
 
  (POST "/publish" [mapping uiConfig excel]
    (services/publish! mapping uiConfig excel))

  (route/resources "/")
  
  (route/not-found "Page not found"))

(def app (handler/site routes))

(comment
  (use 'ring.server.standalone)
  (def handler (handler/site #'routes))
  (defonce server (serve #'handler {:auto-reload? false
                                    :open-browser? false}))
  
  )
