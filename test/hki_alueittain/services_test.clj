(ns hki-alueittain.services_test
  (:require [hki-alueittain.services :refer :all]
            [midje.sweet :refer :all]))

(facts "data for ui"
  (binding [data-path "etc"]
    (let [mapping (get-config "tblDataTiedot-model.yaml")
          ui-config (get-config "tblDataTiedot-config.yaml")
          {:keys [headers rows]} (get-excel-data mapping (str data-path "/tblDataTiedot.xlsx"))
          row (first rows)]
    (data-for-ui row headers ui-config) 
    => [["Väestö ja perheet" 
         [["Äidinkieli ja kansalaisuus" 
           {:headers ["Henkilöä" "%" "Helsingin keskiarvo"], 
            :rows [["Suomenkieliset" "491 524" "82,6 %" "?"] 
                   ["Ruotsinkieliset" "35 537", "6,0 %" "?"]
                   ["Muunkieliset" "68 323", "11,5 %" "?"]
                   ["Ulkomaalaiset" "47 878", "8,0 %" "?"] 
                   ["Ulkomaalaistaustaiset" "70 127", "11,8 %" "?"]]}]]]])))

       
