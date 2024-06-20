import { html } from '@genesislcap/web-core';
import { getDateFormatter, getNumberFormatter } from '../../utils';
import type { FxBlotter } from './fx-blotter';

export const FxBlotterTemplate = html<FxBlotter>`
  <rapid-layout auto-save-key="FX Blotter_1717832859794">
     <rapid-layout-region>
         <rapid-layout-item title="FX Trades">
             <entity-management
                 design-system-prefix="rapid"
                 enable-row-flashing
                 enable-cell-flashing
                 resourceName="ALL_FX_TRADES"
                 createEvent="EVENT_FX_TRADE_INSERT"
                 :createFormUiSchema=${() => (
                     {
               "type": "LayoutVertical2Columns",
               "elements": [
                 {
                   "type": "Control",
                   "label": "Trade Id",
                   "scope": "#/properties/TRADE_ID",
                   "options": {
                     "hidden": true,
                     "readonly": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Trade Version",
                   "scope": "#/properties/TRADE_VERSION",
                   "options": {
                     "hidden": true,
                     "readonly": true
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Trade Status",
                   "scope": "#/properties/TRADE_STATUS",
                   "options": {
                     "hidden": true,
                     "readonly": true
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Side",
                   "scope": "#/properties/SIDE",
                   "options": {
                     "hidden": false,
                     "readonly": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Target Currency",
                   "scope": "#/properties/TARGET_CURRENCY",
                   "options": {
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Notional",
                   "scope": "#/properties/NOTIONAL",
                   "options": {
                     "hidden": false,
                     "readonly": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Source Currency",
                   "scope": "#/properties/SOURCE_CURRENCY",
                   "options": {
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Rate",
                   "scope": "#/properties/RATE",
                   "options": {
                     "hidden": false,
                     "readonly": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Settlement Date",
                   "scope": "#/properties/SETTLEMENT_DATE",
                   "options": {
                     "hidden": false,
                     "readonly": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Client Name",
                   "scope": "#/properties/CLIENT_NAME",
                   "options": {
                    "allOptionsResourceName":'ALL_CLIENTS',
                    "valueField":"CLIENT_NAME",
                    "labelField":"CLIENT_NAME",
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Book Name",
                   "scope": "#/properties/BOOK_NAME",
                   "options": {
                    "allOptionsResourceName":'ALL_BOOKS',
                    "valueField":"BOOK_NAME",
                    "labelField":"BOOK_NAME",
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Entity Name",
                   "scope": "#/properties/ENTITY_NAME",
                   "options": {
                    "allOptionsResourceName":'ALL_ENTITYS',
                    "valueField":"ENTITY_NAME",
                    "labelField":"ENTITY_NAME",
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Trader Name",
                   "scope": "#/properties/TRADER_NAME",
                   "options": {
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Trade Datetime",
                   "scope": "#/properties/TRADE_DATETIME",
                   "options": {
                     "hidden": false,
                     "readonly": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Sales Name",
                   "scope": "#/properties/SALES_NAME",
                   "options": {
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Broker Code",
                   "scope": "#/properties/BROKER_CODE",
                   "options": {
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 }
               ]
             }
                 )}
                 updateEvent="EVENT_FX_TRADE_MODIFY"
                 :updateFormUiSchema=${() => (
                     {
               "type": "LayoutVertical2Columns",
               "elements": [
                 {
                   "type": "Control",
                   "label": "Trade Id",
                   "scope": "#/properties/TRADE_ID",
                   "options": {
                     "hidden": false,
                     "readonly": true
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Trade Version",
                   "scope": "#/properties/TRADE_VERSION",
                   "options": {
                     "hidden": false,
                     "readonly": true
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Trade Status",
                   "scope": "#/properties/TRADE_STATUS",
                   "options": {
                     "hidden": false,
                     "readonly": true
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Side",
                   "scope": "#/properties/SIDE",
                   "options": {
                     "hidden": false,
                     "readonly": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Target Currency",
                   "scope": "#/properties/TARGET_CURRENCY",
                   "options": {
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Notional",
                   "scope": "#/properties/NOTIONAL",
                   "options": {
                     "hidden": false,
                     "readonly": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Source Currency",
                   "scope": "#/properties/SOURCE_CURRENCY",
                   "options": {
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Rate",
                   "scope": "#/properties/RATE",
                   "options": {
                     "hidden": false,
                     "readonly": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Settlement Date",
                   "scope": "#/properties/SETTLEMENT_DATE",
                   "options": {
                     "hidden": false,
                     "readonly": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Client Name",
                   "scope": "#/properties/CLIENT_NAME",
                   "options": {
                    "allOptionsResourceName":'ALL_CLIENTS',
                    "valueField":"CLIENT_NAME",
                    "labelField":"CLIENT_NAME",
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Book Name",
                   "scope": "#/properties/BOOK_NAME",
                   "options": {
                    "allOptionsResourceName":'ALL_BOOKS',
                    "valueField":"BOOK_NAME",
                    "labelField":"BOOK_NAME",
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Entity Name",
                   "scope": "#/properties/ENTITY_NAME",
                   "options": {
                    "allOptionsResourceName":'ALL_ENTITYS',
                    "valueField":"ENTITY_NAME",
                    "labelField":"ENTITY_NAME",
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Trader Name",
                   "scope": "#/properties/TRADER_NAME",
                   "options": {
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Trade Datetime",
                   "scope": "#/properties/TRADE_DATETIME",
                   "options": {
                     "hidden": false,
                     "readonly": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Sales Name",
                   "scope": "#/properties/SALES_NAME",
                   "options": {
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 },
                 {
                   "type": "Control",
                   "label": "Broker Code",
                   "scope": "#/properties/BROKER_CODE",
                   "options": {
                     "hidden": false,
                     "readonly": false,
                     "textarea": false,
                     "isPassword": false
                   }
                 }
               ]
             }
                 )}
                 deleteEvent="EVENT_FX_TRADE_DELETE"
                 :columns=${() => [
                   {
                   field: "TRADE_ID",
                   headerName: "Trade Id",
                   hide: false,
                   }
             ,{
                   field: "TRADE_VERSION",
                   headerName: "Trade Version",
                   hide: false,
                   valueFormatter: getNumberFormatter("0,0", null),
                   }
             ,{
                   field: "TRADE_STATUS",
                   headerName: "Trade Status",
                   hide: false,
                   }
             ,{
                   field: "SIDE",
                   headerName: "Side",
                   hide: false,
                   }
             ,{
                   field: "TARGET_CURRENCY",
                   headerName: "Target Currency",
                   hide: false,
                   }
             ,{
                   field: "NOTIONAL",
                   headerName: "Notional",
                   hide: false,
                   valueFormatter: getNumberFormatter("0,0.00", null),
                   }
             ,{
                   field: "SOURCE_CURRENCY",
                   headerName: "Source Currency",
                   hide: false,
                   }
             ,{
                   field: "RATE",
                   headerName: "Rate",
                   hide: false,
                   valueFormatter: getNumberFormatter("0,0.00000", null),
                   }
             ,{
                   field: "SETTLEMENT_DATE",
                   headerName: "Settlement Date",
                   hide: false,
                   valueFormatter: getDateFormatter("en-GB", {"year":"numeric","month":"short","day":"2-digit","timeZone":"UTC"}),
                   }
             ,{
                   field: "CLIENT_NAME",
                   headerName: "Client Name",
                   hide: false,
                   }
             ,{
                   field: "BOOK_NAME",
                   headerName: "Book Name",
                   hide: false,
                   }
             ,{
                   field: "ENTITY_NAME",
                   headerName: "Entity Name",
                   hide: false,
                   }
             ,{
                   field: "TRADER_NAME",
                   headerName: "Trader Name",
                   hide: false,
                   }
             ,{
                   field: "TRADE_DATETIME",
                   headerName: "Trade Datetime",
                   hide: false,
                   valueFormatter: getDateFormatter("en-GB", {"year":"numeric","month":"short","day":"2-digit","timeZone":"UTC","hour":"2-digit","minute":"2-digit"}),
                   }
             ,{
                   field: "SALES_NAME",
                   headerName: "Sales Name",
                   hide: false,
                   }
             ,{
                   field: "BROKER_CODE",
                   headerName: "Broker Code",
                   hide: false,
                   }
             ] }
                 modal-position="centre"
                 size-columns-to-fit
             ></entity-management>
         </rapid-layout-item>
         <rapid-layout-region type="vertical">
         <rapid-layout-item title="Positions">
             <positions-grid></positions-grid>
         </rapid-layout-item>
        <rapid-layout-item title="Rates">
             <rapid-grid-pro
               enable-row-flashing
               enable-cell-flashing
               >
               <grid-pro-genesis-datasource
                 resource-name="ALL_FX_RATES"
                 :deferredGridOptions=${() => (
                   {
                   columnDefs: [
                   {
                   field: "TARGET_CURRENCY",
                   headerName: "Target Currency",
                   hide: false,
                   }
             ,{
                   field: "SOURCE_CURRENCY",
                   headerName: "Source Currency",
                   hide: false,
                   }
             ,{
                   field: "RATE",
                   headerName: "Rate",
                   hide: false,
                   valueFormatter: getNumberFormatter("0,0.00000", null),
                   }
             ],
                   }
             
                 )}
               >
               </grid-pro-genesis-datasource>
             </rapid-grid-pro>
         </rapid-layout-item>
     </rapid-layout-region>
     </rapid-layout-region>
  </rapid-layout>
`;
