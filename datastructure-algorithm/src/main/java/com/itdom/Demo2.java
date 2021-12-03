package com.itdom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Demo2 {
    public static void main(String[] args) {
        String sql = "SELECT r.id " + " FROM car_service.request_timeout_message r"
                + " INNER JOIN agency_service.persons_cut p ON p.user_id = r.user_id and p.status = 'ENABLED'"
                + " WHERE r.platform_id = :agencyId and r.category = 'ORDER' and p.agency_id = :agencyId AND r.created_dt >= :startDt "
                + " AND r.status = 'UNREAD' AND (r.timeout_tip = 'URGE_ORDER_TIP' OR r.timeout_tip = 'AGENCY_SENDBACK_REQ_TIP' "
                + " OR r.timeout_tip = 'OUT_AGENCY_SENDBACK_REQ_TIP' OR (r.vip_id IS NOT NULL AND EXISTS (SELECT cs.id FROM agency_service.customers cs "
                + " LEFT JOIN agency_service.customer_types ct ON cs.customer_type_id=ct.id AND cs.id= r.vip_id AND ct.code='PINGAN_ZV'))"
                + " AND (p.id = :personId " + "OR ((r.user_id = 1 AND r.person_ids is null) OR (r.user_id = 1 AND r.person_ids is not null AND position('" + 115170
                + "' in r.person_ids) > 0)))";

        System.out.println(sql);
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        System.out.println(commonMessageCondition4Platform(115170L));


    }

    public static StringBuilder commonMessageCondition4Platform(Long personId) {
        return new StringBuilder()
                .append("and msg.platform_id = :platformId ")
                .append("and msg.category = 'ORDER' ")
                .append("and (msg.user_id = :userId ")
                .append("	or ((msg.user_id = 1 and msg.person_ids is null or (msg.user_id = 1 and msg.person_ids is not null and '").append(personId).append("' = any(string_to_array(msg.person_ids, ','))) ) ")
                .append("		and (msg.timeout_tip in ('NEW_WEIXIN_TIP','NEW_OUTER_REQ_TIP','NEW_COMMON_TIP','VI_NEW_TASK_TIP' ,'URGE_ORDER_TIP','HANDLE_TIMEOUT_TIP','DISPATCH_TIMEOUT_TIP','AGENCY_HANDLE_TIMEOUT_TIP','AGENCY_DISPATCH_TIMEOUT_TIP','AGENCY_SENDBACK_REQ_TIP','TRANSFER_TASK_TIP') ")
                .append("			or msg.vip_id is not null or msg.timeout_tip in ('PERSON_CANCEL_TIP', 'USER_CANCEL_TIP') and msg.is_auto_second_dispatched is true) ")
                .append("	) ")
                .append(") ");
    }

}
