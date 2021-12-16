package com.itdom;


import java.util.HashMap;
import java.util.Map;

public class Sample {
    public static void main(String[] args) {

        StringBuilder sql = new StringBuilder();
        sql.append("WITH person_regions AS (")
                .append("   SELECT pr.person_id, pr.region_id, r.level FROM security_service.person_regions pr ")
                .append("   INNER JOIN common_service.region_news r ON r.id = pr.region_id")
                .append(")")
                .append("SELECT req.*, vi.* FROM car_service.vi_requests vi ")
                .append("    LEFT JOIN car_service.service_reqs req ON vi.id = req.id ");


        System.out.println(sql);
        Long cwsTimeout = 90L;
        Long agencyTimeout = 300L;
        StringBuilder listHead = new StringBuilder();
        listHead.append("WITH person_regions AS ("
                + "SELECT pr.person_id, pr.region_id, r.level FROM security_service.person_regions pr "
                + "INNER JOIN common_service.region_news r ON r.id = pr.region_id WHERE pr.person_id = :currentPersonId) ");
        listHead.append(", person_agencies_auth AS ("
                + "SELECT pa.person_id, pa.agency_id FROM security_service.person_agency pa WHERE pa.person_id = :currentPersonId) ");
        listHead.append("SELECT req.id,req.source_agency_id,req.created_by,req.modified_by,req.total_asset_count,"
                + "req.modified_dt,req.person_id,req.agency_id,req.accepted_dt, req.status, req.request_result, req.audit_status, req.car_id,req.service_id,"
                + "req.cellphone,req.request_source,req.car_owner_uid,req.car_owner_name,req.request_code,req.case_code,"
                + "req.created_dt,req.flags,req.appraise_val,req.person_name AS person_name,req.plate_number,req.vip_id,CASE WHEN (vpn.id is null) THEN null ELSE req.vip_type END AS vip_type,(SELECT CASE WHEN asct.prefix is null THEN vpn.remark ELSE CONCAT(asct.prefix,asct.name) END FROM agency_service.customer_types  asct WHERE asct.id=vpn.customer_type_id) as vip_info ,"
                + "req.data_source,req.is_issue_task,req.is_in_coverage,req.complaints_level,(select short_name from agency_service.agencies where ID = req.cooperation_agency_id) as cooperationAgencyName,"
                + "req.is_need_garage,req.salvation_type,detail.redispatch_cancel_type,"
                + "req.cooperation_agency_id cooperationAgencyId,(select NAME from agency_service.agencies where ID = req.dispatch_agency_id) as dispatchAgencyName,req.dispatch_agency_id dispatchAgencyId,req.assist_dispatch_agency_id assistDispatchAgencyId,req.created_by_agency,"
                + "case when req.created_by_agency != 1 and (req.created_by_agency != req.agency_id or req.agency_id is null) then (select name from agency_service.agencies where id = req.created_by_agency limit 1) else null end as created_by_agency_name, "// 仅子平台第三方订单需要
                + "req.high_frequency highFrequency,req.is_need_trailer,req.out_dispatch_agency_id outDispatchAgencyId,"
                + "(select NAME from agency_service.agencies where ID = req.out_dispatch_agency_id) as outDispatchAgencyName,"

				/*+ "COALESCE(charges2.paid,charges1.paid) paid,"
				+ "COALESCE(charges2.channel,charges1.channel) channel, "*/
                + "case when (charges1.paid is true or charges2.paid is true or sop.is_offline_charges is true and sop.is_received_cash_by_rescuer is true) then true else false end as paid,"
                + "case when charges1.paid is true then charges1.channel when charges2.paid is true then charges2.channel "
                + "		when sop.is_offline_charges is true and sop.offine_fee > 0 then 'offline' "
                + "		else COALESCE(charges2.channel,charges1.channel) end as channel,"

                + "req.accept_channel,req.is_auto_second_dispatched, req.auto_dispatch_type, req.current_dispatch_uid, "
                + " CASE  WHEN (req.status ='DISPATCHED' AND extract (epoch FROM (now() - req.action_start_time)) > "
                + cwsTimeout + ") then 3 ")
                .append("WHEN (req.status = 'HANDLED' AND extract (epoch FROM (now() - req.action_start_time)) > "
                        + cwsTimeout + ") then 2 ")
                .append("WHEN (req.status  ='PENDING' AND extract (epoch FROM (now() - req.action_start_time)) > "
                        + cwsTimeout + ") then 1 ")
                .append("WHEN (req.status  ='AGENCY_PENDING' AND extract (epoch FROM (now() - detail.agency_dispatch_time)) > "
                        + agencyTimeout + ") then 4 ")
                .append("WHEN (req.status  ='AGENCY_HANDLED' AND extract (epoch FROM (now() - detail.agency_dispatch_time)) > "
                        + agencyTimeout + ") then 5 ")
                .append("ELSE 0 END AS timeOutType, ")

                .append("req.urge_count,").append("req.exclusive_motorcade_id,")
                .append("CASE WHEN (req.created_by_agency = 1) THEN false ELSE true END AS isAgencyCreated,")
                .append("(select count(sup.id) from car_service.service_request_assets_sup sup where sup.request_id = req.id and is_del is not true) as supplement_asset_count, ")
                .append("req.settle_agency_id, ")
                // by 2.85.0
                .append("case when sop.is_bargained_price is true then sop.payables else null end as bargain_price, ")
                .append("case when sop.is_bargained_price is true then sop.bargain_status else null end as bargain_status, ")
                .append("(select name from user_service.users where id = req.current_dispatch_uid) as dispatcher ")
                //by 2.87.0
                .append(" ,req.destination")
                .append(" ,req.location_descr ")
                //by 2.99.2
                .append(" ,case when req.data_source=21 and req.request_source='AGENCY_SOURCE' then cast(req.ext_json as json)->>'registNo' else null end as regist_no ");
        listHead.append(" ,CASE WHEN (SELECT id FROM car_service.request_report_prepared WHERE request_id = req.ID AND status='ENABLED' AND cyy_status = 'COMMIT' LIMIT 1 ) IS NULL THEN false ELSE true END AS report_prepared_flags ");

        System.out.println(listHead.toString());




    }
}
