查詢某個時段的公車資料
SELECT * FROM busdb.busdata WHERE datatime >= '2016-02-10 13:58:00' AND datatime < '2016-02-10 13:59:00';

查詢266的公車資料
包含266(北投-市府)及266
266%指的是266開頭的routename 
like會進行字串比對
前面要先加上時間限制查詢範圍 否則查詢時間會很久
SELECT * FROM busdb.busdata WHERE datatime >= '2016-02-10 13:58:00' AND datatime < '2016-02-10 13:59:00' AND routename LIKE '266%';

查詢349-FP的公車資料
使用'='查詢
SELECT * FROM busdb.busdata WHERE datatime >= '2016-02-10 13:58:00' AND datatime < '2016-02-10 13:59:00' AND busid = '349-FP';

這時候發現字串比對比較慢，建議以與busid相對應的carid來做查詢，會節省一點時間
SELECT * FROM busdb.busdata WHERE datatime >= '2016-02-10 13:58:00' AND datatime < '2016-02-10 13:59:00' AND carid = '222233749';