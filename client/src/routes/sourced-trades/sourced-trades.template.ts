import { html } from '@genesislcap/web-core';
import { getDateFormatter, getNumberFormatter, cellStyle } from '../../utils';
import type { SourcedTrades } from './sourced-trades';

export const SourcedTradesTemplate = html<SourcedTrades>`
  <rapid-layout auto-save-key="Sourced Trades_1717832859794">
     <rapid-layout-region type="horizontal">
         <rapid-layout-region type="vertical">
             <rapid-layout-item title="Loans">
                 <entity-management
                     design-system-prefix="rapid"
                     enable-row-flashing
                     enable-cell-flashing
                     resourceName="ALL_LOAN_TRADES"
                     deleteEvent="EVENT_LOAN_TRADE_DELETE"
                     :columns=${() => [
                       {
                       field: "LOAN_ID",
                       headerName: "Loan Id",
                       hide: false,
                       }
                 ,{
                       field: "CLIENT_NAME",
                       headerName: "Client Name",
                       hide: false,
                       }
                 ,{
                       field: "FACILITY_NAME",
                       headerName: "Facility Name",
                       hide: false,
                       }
                 ,{
                       field: "FACILITY_CURRENCY",
                       headerName: "Facility Currency",
                       hide: false,
                       }
                 ,{
                       field: "FACILITY_AMOUNT",
                       headerName: "Facility Amount",
                       hide: false,
                       valueFormatter: getNumberFormatter("0,0.00", null),
                       }
                 ,{
                       field: "PAYMENT_CURRENCY",
                       headerName: "Payment Currency",
                       hide: false,
                       }
                 ,{
                       field: "PAYMENT_AMOUNT",
                       headerName: "Payment Amount",
                       hide: false,
                       valueFormatter: getNumberFormatter("0,0.00", null),
                       cellStyle: (params) => cellStyle(params),
                       }
                 ,{
                       field: "PAYMENT_DATE",
                       headerName: "Payment Date",
                       hide: false,
                       valueFormatter: getDateFormatter("en-GB", {"year":"numeric","month":"short","day":"2-digit","timeZone":"UTC"}),
                       }
                 ,{
                       field: "DRAWDOWN_CURRENCY",
                       headerName: "Drawdown Currency",
                       hide: false,
                       }
                 ,{
                       field: "DRAWDOWN_AMOUNT",
                       headerName: "Drawdown Amount",
                       hide: false,
                       valueFormatter: getNumberFormatter("0,0.00", null),
                       cellStyle: (params) => cellStyle(params),
                       }
                 ,{
                       field: "DRAWDOWN_DATE",
                       headerName: "Drawdown Date",
                       hide: false,
                       valueFormatter: getDateFormatter("en-GB", {"year":"numeric","month":"short","day":"2-digit","timeZone":"UTC"}),
                       }
                 ] }
                     modal-position="centre"
                     size-columns-to-fit
                 ></entity-management>
             </rapid-layout-item>
             <rapid-layout-item title="CDs">
                 <entity-management
                     design-system-prefix="rapid"
                     enable-row-flashing
                     enable-cell-flashing
                     resourceName="ALL_CD_TRADES"
                     deleteEvent="EVENT_CD_TRADE_DELETE"
                     :columns=${() => [
                       {
                       field: "CD_ID",
                       headerName: "Cd Id",
                       hide: false,
                       }
                 ,{
                       field: "CLIENT_NAME",
                       headerName: "Client Name",
                       hide: false,
                       }
                 ,{
                       field: "DEPOSIT_DATE",
                       headerName: "Deposit Date",
                       hide: false,
                       valueFormatter: getDateFormatter("en-GB", {"year":"numeric","month":"short","day":"2-digit","timeZone":"UTC"}),
                       }
                 ,{
                       field: "DEPOSIT_CURRENCY",
                       headerName: "Deposit Currency",
                       hide: false,
                       }
                 ,{
                       field: "DEPOSIT_AMOUNT",
                       headerName: "Deposit Amount",
                       hide: false,
                       valueFormatter: getNumberFormatter("0,0.00", null),
                       cellStyle: (params) => cellStyle(params),
                       }
                 ,{
                       field: "DEPOSIT_RATE",
                       headerName: "Deposit Rate",
                       hide: false,
                       valueFormatter: getNumberFormatter("0,0.00000", null),
                       }
                 ,{
                       field: "MATURITY_DATE",
                       headerName: "Maturity Date",
                       hide: false,
                       valueFormatter: getDateFormatter("en-GB", {"year":"numeric","month":"short","day":"2-digit","timeZone":"UTC"}),
                       }
                 ,{
                       field: "MATURITY_AMOUNT",
                       headerName: "Maturity Amount",
                       hide: false,
                       valueFormatter: getNumberFormatter("0,0.00", null),
                       cellStyle: (params) => cellStyle(params),
                       }
                 ] }
                     modal-position="centre"
                     size-columns-to-fit
                 ></entity-management>
             </rapid-layout-item>
         </rapid-layout-region>
         <rapid-layout-region type="vertical">
             <rapid-layout-item title="Loans by Client - GBP">
                 <rapid-g2plot-chart
                   type="pie"
                   :config="${(x) => ({
                       radius: 0.75,
                       angleField: 'value',
                       colorField: 'groupBy',
                   })}"
                 >
                   <chart-datasource
                     resourceName="LOAN_PAYMENTS_GBP"
                     server-fields="CLIENT_NAME PAYMENT_AMOUNT"
                   ></chart-datasource>
                 </rapid-g2plot-chart>
             </rapid-layout-item>
             <rapid-layout-item title="Loans by Currency - GBP Equivalent">
                 <rapid-g2plot-chart
                   type="bar"
                   :config="${(x) => ({
                       xField: 'value',
                       yField: 'groupBy',
                       seriesField: 'groupBy',
                       barWidthRatio: 0.8,
                   })}"
                 >
                   <chart-datasource
                     resourceName="LOAN_PAYMENTS_GBP"
                     server-fields="PAYMENT_CURRENCY PAYMENT_AMOUNT"
                   ></chart-datasource>
                 </rapid-g2plot-chart>
             </rapid-layout-item>
             <rapid-layout-item title="CDs by Client">
                 <rapid-g2plot-chart
                   type="pie"
                   :config="${(x) => ({
                       radius: 0.75,
                       angleField: 'value',
                       colorField: 'groupBy',
                   })}"
                 >
                   <chart-datasource
                     resourceName="ALL_CD_TRADES"
                     server-fields="CLIENT_NAME DEPOSIT_AMOUNT"
                   ></chart-datasource>
                 </rapid-g2plot-chart>
             </rapid-layout-item>
         </rapid-layout-region>
     </rapid-layout-region>
  </rapid-layout>
`;
