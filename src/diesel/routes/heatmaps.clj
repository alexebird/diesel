(ns diesel.routes.heatmaps
  (:require [compojure.core :refer :all]
            [diesel.layout :as layout]
            [diesel.util :as util]))

;;ENTITIES = {
;; 'show'   => { filter_field:   'show_date',
;;               auto_timeframe: (Rails.env.production? ? 'trailing_week' : 'all_time')},
;; 'year'   => { filter_field:   'year',
;;               auto_timeframe: 'all_time'},
;; 'tour'   => { filter_field:   'tour_name',
;;               auto_timeframe: (Rails.env.production? ? 'trailing_month' : 'all_time')},
;; 'venue'  => { filter_field:   'venue_name',
;;               auto_timeframe: 'all_time'},
;; 'tours'  => { filter_field:   nil,
;;               auto_timeframe: 'all_time'},
;; 'years'  => { filter_field:   nil,
;;               auto_timeframe: 'all_time'},
;; 'venues' => { filter_field:   nil,
;;               auto_timeframe: 'all_time'}
  
(defn production? []
  true)

(def entities
  {:show {:filter-field "show_date"
          :auto-timeframe (if (production?) "trailing_week" "all_time")}}
  {:year {:filter-field "year"
          :auto-timeframe "all_time"}}
  {:tour {:filter-field "tour_name"
          :auto-timeframe (if (production?) "trailing_month" "all_time")}}
  {:venue {:filter-field "venue_name"
           :auto-timeframe "all_time"}}
  {:tours {:filter-field nil
           :auto-timeframe "all_time"}}
  {:years {:filter-field nil
           :auto-timeframe "all_time"}}
  {:venues {:filter-field nil
           :auto-timeframe "all_time"}})

(def heatmap-query-1
  {:entity "year"
   :fitler "2014"
   :timeframe "auto"
   :timeframe {:start #inst "2014-01-01T10:56:00" :end #inst "2014-01-05T15:12:00"}
   :timezone "Central Time (US & Canada)"})

(defn heatmap-endpoint []
  "heatmap")

(defroutes heatmap-routes
  (POST "/heatmaps" [] heatmap-endpoint))
