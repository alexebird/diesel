(ns diesel.querying
  "This is APIv3 stuff. Handles mapping of querying to internal data stuctures.")


(def example-query-1 {:stats_query {
                                    :entity "tracks"
                                    :timeframe "last_hour"
                                    :filters [{:attr_name "tour_name" :operator "eq" :attr_value: "2013-14 New Years Run"}]
                                    :timezone "Central Time (US & Canada)"
                                    :stats [
                                            {:name "play_count"}
                                            {:name "unique_count"}
                                            {:name "duration_sum_fmt"}
                                            {:name "duration_sum_sec"}
                                            {:name "catalog_progress"}
                                            {:name "total_count"}
                                            {:name "play_count_ranking" :limit 100 :offset 0}]}})
