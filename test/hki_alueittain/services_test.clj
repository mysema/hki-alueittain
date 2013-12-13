(ns hki-alueittain.services_test
  (:require [hki-alueittain.services :refer :all]
            [midje.sweet :refer :all]))

(facts "data for ui"
  (binding [data-path "etc"]
    (let [mapping (get-config "tblDataTiedot-config.yaml")
          ui-config (get-config "tblDataTiedot-ui-config.yaml")
          {:keys [headers rows]} (get-excel-data mapping (str data-path "/tblDataTiedot.xlsx"))
          row (rest (first rows))]
    (data-for-ui row headers ui-config) 
    => [["Väestö ja perheet" 
         [["Äidinkieli ja kansalaisuus" 
           {:headers ["Henkilöä" "%"], 
            :rows [["Suomenkieliset" 82.6 35537.0] 
                   ["Ruotsinkieliset" 6.0 68323.0] 
                   ["Muunkieliset" 11.5 47878.0]
                   ["Ulkomaalaiset" 8.0 70127.0] 
                   ["Ulkomaalaistaustaiset" 11.8 247087.0]]}]]]])))

       
