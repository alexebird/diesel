(ns diesel.querying
  (:require [clojurewerkz.elastisch.rest          :as esr]
            [clojurewerkz.elastisch.rest.document :as esd]
            [clojurewerkz.elastisch.query         :as q]
            [clojurewerkz.elastisch.rest.response :as esrsp]
            [clojure.pprint :refer [pprint]]))


;; (def example-query-1 {:stats_query {
;;                                     :entity "tracks"
;;                                     :timeframe "last_hour"
;;                                     :filters [{:attr_name "tour_name" :operator "eq" :attr_value "2013-14 New Years Run"}]
;;                                     :timezone "Central Time (US & Canada)"
;;                                     :stats [
;;                                             {:name "play_count"}
;;                                             {:name "unique_count"}
;;                                             {:name "duration_sum_fmt"}
;;                                             {:name "duration_sum_sec"}
;;                                             {:name "catalog_progress"}
;;                                             {:name "total_count"}
;;                                             {:name "play_count_ranking" :limit 100 :offset 0}]}})

;; (def example-query-2 {:stats_query {
;;                                     :entity "tracks"
;;                                     :timeframe "auto"
;;                                     :filters [{:attr_name "tour_name" :operator "eq" :attr_value "2013-14 New Years Run"}]
;;                                     :timezone "Central Time (US & Canada)"
;;                                     :stats [""]}})

;; (defn accept-v3-query
;;   "do something with a v3-style query."
;;   [query]
;;   ;; TODO validation
;;   )

(def entities
  {:track {:top-plays-field "track.unique_slug"}
   :show  {:top-plays-field "track.show.date"}})

(def conn (esr/connect "http://127.0.0.1:9200"))

(defn _source [q]
  (merge q {:_source {}} ))

(defn query [q]
  (merge q {:query {
                    :filtered {
                               :query {
                                       :function_score {
                                                        :script_score {:script "pts-play-score"}
                                                        :boost_mode "replace"}}}}}))

(defn _sort [q]
  (merge q {:sort [{:_score {:order "desc"}}]}))

(defn- add-agg [q agg-key agg-val]
  (assoc-in q [:aggs agg-key] agg-val))

(defn agg-play-count [q]
  (add-agg q :pc {:sum {:script "pts-play-count"}}))

(defn agg-dur [q]
  (add-agg q :dur {:sum {:script "pts-total-plays-duration"}}))

(defn agg-top-plays [q term-field]
  (add-agg q :top_plays {
                         :terms {
                                 :field term-field
                                 :order {:play_score_sum "desc"}}
                         :aggs {
                                :top_plays_hits {:top_hits {:size 1}}
                                :play_score_sum {:sum {:script "_score"}}}}))

(defn query [{top-plays-field :top-plays-field}]
  (-> {}
      _source
      query
      _sort
      (agg-top-plays top-plays-field)
      agg-play-count
      agg-dur))

(defn run-query [q]
  (let [res (esd/search conn "plays.exp.1" "play" q)]
    res))

(defn wrangle-play [play]
  {(or (-> play :key_as_string) (-> play :key))
   (-> play :play_score_sum :value)})

(defn wrangle [results]
  (pprint
   (map wrangle-play
        (get-in results [ :aggregations :top_plays :buckets]))))
