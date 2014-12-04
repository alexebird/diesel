(ns diesel.routes.home
  (:require [compojure.core :refer :all]
            [diesel.layout :as layout]
            [diesel.util :as util]))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (POST "/heatmaps" [] "omg"))
