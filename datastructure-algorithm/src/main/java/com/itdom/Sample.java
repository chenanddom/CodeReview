package com.itdom;



public class Sample {
    public static void main(String[] args) {

//        String sql = "SELECT "
//                + " sum(CASE WHEN (r.status = 'PENDING' OR r.status = 'AGENCY_PENDING') THEN 1 ELSE 0 END) as pending_task_count,"
//                + " sum(CASE WHEN (r.status = 'HANDLED' OR r.status = 'AGENCY_HANDLED' OR r.status = 'DISPATCHED') THEN 1 ELSE 0 END) as un_dispatched_task_count,"
//                + " sum(CASE WHEN (r.person_id is not null AND r.status IN ('PROCESSING','TELEPHONE','PEND_PROCESSING','START_TRAILER','END_TRAILER','ACCEPTED' )) THEN 1 ELSE 0 END) as processing_task_count,"
//                + " sum(CASE WHEN (r.audit_status = 'SUPPLEMENT') THEN 1 ELSE 0 END) as supplement_count"
//                //+ " FROM car_service.service_reqs r "
//                + " FROM car_service.service_reqs r "
//                //+ " LEFT JOIN car_service.service_reqs_ids reqs_ids ON r.id = reqs_ids.id  "
//                + " LEFT JOIN car_service.service_reqs_sub_platform reqs_sp ON r.id = reqs_sp.req_id  "
//                + " WHERE ((r.person_id IS NOT NULL AND reqs_sp.status IN ('PROCESSING','TELEPHONE','PEND_PROCESSING','START_TRAILER','END_TRAILER','ACCEPTED')) " +
//                "OR reqs_sp.audit_status='SUPPLEMENT' OR reqs_sp.status IN ('HANDLED','AGENCY_HANDLED','DISPATCHED','PENDING','AGENCY_PENDING')) " +
//                " AND (r.agency_id = :agencyId OR r.created_by_agency = :agencyId OR dispatch_agency_id = :agencyId) and reqs_sp.created_dt >= now() + '" + "-40 d"
//                + "' and reqs_sp.is_deleted is not null AND reqs_sp.is_deleted = FALSE AND reqs_sp.type = 'SR' AND r.redispatch_id is null "
//                //+ " AND reqs_ids.ids @> ARRAY [ CAST (:agencyId AS int8) ] ";
//                + " AND MOD(CAST(reqs_sp.rel_agency_id AS BIGINT),8)=MOD(CAST(:agencyId AS BIGINT),8) AND reqs_sp.rel_agency_id = :agencyId ";

/*        			String sql = "SELECT r.id " + " FROM car_service.request_timeout_message r "
					+ " INNER JOIN agency_service.persons_cut p ON p.user_id = r.user_id and p.status = 'ENABLED'  "
					+ " WHERE r.platform_id = :agencyId and r.category = 'ORDER' and p.agency_id = :agencyId AND r.created_dt >= :startDt "
					+ "    AND r.status = 'UNREAD' AND (r.timeout_tip = 'NEW_WEIXIN_TIP' OR r.timeout_tip = 'NEW_OUTER_REQ_TIP' OR r.timeout_tip = 'VI_NEW_TASK_TIP') "
					+ "     AND (p.id = :personId " + "       OR ((r.user_id = 1 AND r.person_ids is null) "
					+ "            OR (r.user_id = 1 AND r.person_ids is not null AND position('" + 1
					+ "' in r.person_ids) > 0)) " + "        ) ";*/
        //User Story #17717 做为调度人员，除催单、至尊VIP下单、服务商退回订单外，其他的消息类型我不再可以听到响铃提醒
        String sql = "SELECT r.id " + " FROM car_service.request_timeout_message r"
                + " INNER JOIN agency_service.persons_cut p ON p.user_id = r.user_id and p.status = 'ENABLED'"
                + " WHERE r.platform_id = :agencyId and r.category = 'ORDER' and p.agency_id = :agencyId AND r.created_dt >= :startDt "
                + " AND r.status = 'UNREAD' AND (r.timeout_tip = 'URGE_ORDER_TIP' OR r.timeout_tip = 'AGENCY_SENDBACK_REQ_TIP' "
                + " OR (r.vip_id IS NOT NULL AND EXISTS (SELECT cs.id FROM agency_service.customers cs "
                + " LEFT JOIN agency_service.customer_types ct ON cs.customer_type_id=ct.id AND cs.id= r.vip_id AND ct.code='PINGAN_ZV')))"
                + " AND (p.id = :personId " + "OR ((r.user_id = 1 AND r.person_ids is null) OR (r.user_id = 1 AND r.person_ids is not null AND position('" + "personId"
                + "' in r.person_ids) > 0)))";

        System.out.println(sql);

    }
}
