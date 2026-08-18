#!/usr/bin/env nbb
;; The portable suite on nbb — no build step, no JVM.
;;
;; This file is the point of the `.cljc`. A reader conditional whose `:cljs`
;; branch nothing evaluates is the appearance of portability, not
;; portability — a check that cannot fail.
;;
;;   nbb --classpath src:test test/run_portable.cljs
;;
;; And it is run from a FOREIGN working directory in CI and by hand:
;;
;;   cd /tmp/elsewhere && nbb --classpath /path/to/src:/path/to/test \
;;     /path/to/test/run_portable.cljs
;;
;; That is not ceremony. `kotoba-lang/technology` measured on 2026-08-18 that
;; a cwd-relative `resources/` read returns nil for every row the moment the
;; library is a dependency rather than the root project. This library reads
;; its tables from `kotoba.shokyaku.embedded`, a generated projection with a
;; `--check` gate, precisely so that the foreign-cwd run gives the same
;; answers as the local one.
;;
;; Every `deftest`-bearing portable namespace must be named BOTH in the
;; require and in `run-tests`: requiring registers the vars, only `run-tests`
;; runs them, and a runner naming a subset prints the same `Ran N tests`
;; shape as one naming all of them.
(require '[cljs.test :as t]
         '[kotoba.shokyaku-test])

(defmethod t/report [:cljs.test/default :end-run-tests] [m]
  (when-not (t/successful? m) (set! (.-exitCode js/process) 1)))

(t/run-tests 'kotoba.shokyaku-test)
