;;; # Data Visualization in Clojure
(ns clojure-datavis.core
  (:require [nextjournal.clerk :as clerk]
            [tech.v3.dataset :as ds]))

;; ## An exploration of data visualization in Clojure

;; This clojure file is formatted by Clerk when it is saved.
;; I am editing the file in VS Code using the Calva extension.
;; Calva suggests saving optional code into `(comment ...)` forms, so that you can interactively choose to evaluate pieces when wanted.

(comment
  ;; If you use :watch-paths, clerk will re-run the file each time it is safed.
  (clerk/serve!
   {:browse? true
    :watch-paths ["src"]})
  ;; If you don't want to auto-run the file, do not watch-paths and explcitly call "show!" when desired.
  (clerk/serve! {:browse true})
  (clerk/show! "src/clojure_datavis/core.clj")
  :rcf)

;; Clerk will show the results of defs 

(def hello-world "Hello, World!")

hello-world

;; Let's get started.

;; Prepare to get the CSV files.
;; But test to see if we already got it, to avoid un-needed downloads. 
;;
;; If a java function takes variable args, then in Clojure you need to pass the variable args as an explicitly typed array 
;; * `java.nio.file.Paths#get` takes (`String`, `String...`)

(import '[java.nio.file Files Paths LinkOption])
(defn get-url-to-file
  "Get a url to a file, but check the file first"
  [url filename]
  (let [path (Paths/get filename (into-array String []))]
    (if-not
     (and (Files/exists path (into-array LinkOption []))
          (Files/isRegularFile path (into-array LinkOption [])))
      (spit filename
            (slurp url)))
    (str "Got " path)))

;; To control when Clerk re-evaluates the form,
;; use `_run-at` to freeze the hashed value of the form.
;; The date doesn't matter - the imporant thing is that the form hashes to a stable value.  To re-run the form, change the date.

(let [_run-at #_(java.util.Date.) #inst "2023-01-17T12:00:00.000-00:00"]
  (get-url-to-file
   "https://github.com/CSSEGISandData/COVID-19/raw/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv"
   "confirmed.csv"))

(let [_run-at #_(java.util.Date.) #inst "2023-01-17T12:00:00.000-00:00"]
  (get-url-to-file
   "https://github.com/CSSEGISandData/COVID-19/raw/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv"
   "deaths.csv"))

(let [_run-at #_(java.util.Date.) #inst "2023-01-17T12:00:00.000-00:00"]
  (get-url-to-file
   "https://github.com/CSSEGISandData/COVID-19/raw/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv"
   "recovered.csv"))

;; Using [tech.ml.dataset](https://github.com/techascent/tech.ml.dataset)

;; okay let's load the csv files
(def deaths (ds/->dataset "deaths.csv"))
(def confirmed (ds/->dataset "confirmed.csv"))
(def recovered (ds/->dataset "recovered.csv"))

;; There are a lot of options for slicing data.  Here is some experimentation.

; a map of column-name to column-value, take 2 rows

(take 2 (ds/mapseq-reader deaths))

; select the "Country/Region" column, then take 2 rows of it.

(take 2 (deaths "Country/Region"))

; take the first four columns, then get their metadata

(->> deaths
     ds/columns
     (take 5)
     (map (fn [column]
            (meta column))))

; Destructure dataset like a map 
; https://clojure.org/guides/destructuring

(for [[k column] deaths]
  [k (meta column)])


(let [{province-state "Province/State",
       country-region "Country/Region",
       lat "Lat",
       lon "Long"} deaths]
  [symbol (meta province-state)])


(let [names (ds/column-names deaths)]
  (nth names 5))


(drop 4
      (ds/select
       deaths
       (take 7 (ds/column-names deaths))
       [0 1 2 3 4 5 6]))


;; ## Okay I think I'm ready to plot this

^{::clerk/viewer clerk/table}
(ds/select-by-index deaths (range 6) (range 65 70))



^{::clerk/viewer clerk/table
  ::clerk/visibility {:result :hide}}
(def series
  (into []
        (for [col (drop 4 (ds/column-names deaths))]
          {:date
           col
           :death-series
           (ds/filter-column
            (ds/select-columns
             deaths
             ["Lat" "Long" col])
            col #(not (= % 0)))
           :confirmed-series
           (ds/filter-column
            (ds/select-columns
             confirmed
             ["Lat" "Long" col])
            col #(not (= % 0)))
           :recovered-series
           (ds/filter-column
            (ds/select-columns
             recovered
             ["Lat" "Long" col])
            col #(not (= % 0)))})))

;; like the other demos, let's take item 120
(def series-120 (nth series 120))
(def date-120 (:date series-120))
(def plot-series (:recovered-series series-120))
(def values (ds/select-columns-by-index plot-series [-1]))

(ds/mapseq-reader plot-series)

;; test can I figure out how to use PLotly

;; https://stackoverflow.com/questions/63877348/how-do-i-set-dot-sizes-and-colors-for-a-plotly-express-scatter-geo

(clerk/plotly
 {:data [{:lat [24 22]
          :lon [60 92]
          :subreg [62 93]
          :type "scattergeo"}]
  :layout {:title "Test Plot"}
  :config {:displayLogo false}})

;; okay now we're getting somewhere


(clerk/plotly
 {:data [{:lat (seq (plot-series "Lat"))
          :lon (seq (plot-series "Long"))
          :text (seq (plot-series date-120))
          :type "scattergeo"
          :marker {:symbol "diamond"
                   :color "#DC7633"}}]
  :layout {:font {:color "rgb(240,240,240)"}
           :title (str "COVID-19 recoveries (" date-120 ")")
           :paper_bgcolor "rgb(60,60,60)"
           :geo {:scope "world"
                 :showland true
                 :showcountries true
                 :bgcolor "rgb(90,90,90)"
                 :landcolor "rgb(250,250,250)"}}
  :config {:displayLogo false}})

;; GOOD ENOUGH

