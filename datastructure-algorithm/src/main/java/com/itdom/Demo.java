package com.itdom;

import com.sun.xml.internal.ws.util.StringUtils;

public class Demo {
    public static void main(String[] args) {

        String sql = "SELECT COUNT(r.id) FROM car_service.request_timeout_message r  "
                + " INNER JOIN agency_service.persons_cut p ON p.user_id = r.user_id and p.status = 'ENABLED'  "
                + " WHERE r.platform_id = :agencyId and r.category = 'ORDER' and p.agency_id = :agencyId AND r.created_dt >= :startDt   AND r.status = 'UNREAD' "
                + "     AND (p.id = :personId OR (r.user_id = 1 AND (r.person_ids is null OR position('" + 111003
                + "' in r.person_ids) > 0)) "
                + "       AND (r.timeout_tip IN ('NEW_WEIXIN_TIP','NEW_OUTER_REQ_TIP','VI_NEW_TASK_TIP','URGE_ORDER_TIP','HANDLE_TIMEOUT_TIP','DISPATCH_TIMEOUT_TIP','AGENCY_HANDLE_TIMEOUT_TIP','AGENCY_DISPATCH_TIMEOUT_TIP','AGENCY_SENDBACK_REQ_TIP', 'TRANSFER_TASK_TIP')"
                + " OR r.NEW_WEIXIN_TIP_id IS NOT NULL OR r.timeout_tip IN ('PERSON_CANCEL_TIP', 'USER_CANCEL_TIP') AND r.is_auto_second_dispatched IS TRUE ))";
        System.out.println(sql);

        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        sql = "SELECT COUNT(id) FROM " + "(SELECT r.id  " + "FROM car_service.request_timeout_message r  "
                + "        INNER JOIN agency_service.persons_cut p ON p.user_id = r.user_id  "
                + "WHERE r.platform_id = :agencyId and r.category = 'ORDER' and p.agency_id = :agencyId  AND r.created_dt >= :startDt"
                + "        AND r.status = 'UNREAD'  " + "        AND p.id = :personId  "
                + "and 1 > 2 "+

                "UNION  " +
                // 二调订单, 待补充资料, 预约提醒(2.76.0)
                "SELECT r.id  " + "FROM car_service.request_timeout_message r  "
                + "WHERE r.platform_id = :agencyId and r.category = 'ORDER' and r.user_id = 1 AND r.created_dt >= :startDt AND r.status = 'UNREAD' "
                + "    AND r.timeout_tip IN ('NEW_AGENCY_REQ_TIP', 'REQUEST_SUPPLEMENT_TIP','REQUEST_FEEDBACK_HANDLE_TIP', 'APPOINTMENT_NOTIFICATION_TIP') "
                + "    AND (r.person_ids is null OR position('"
                + 111003 + "' in r.person_ids) > 0)  "
                + "and 1 > 2"

                + "UNION "
                + "SELECT r.id FROM car_service.request_timeout_message r  "
                + "WHERE r.platform_id = :agencyId and r.category = 'FINANCE' AND r.status = 'UNREAD'  "
                + "and 1 > 2"

                + "UNION "
                + "SELECT r.id FROM car_service.request_timeout_message r  "
                + "        INNER JOIN agency_service.persons_cut p ON CAST(p.id AS TEXT)= r.person_ids  "
                + "WHERE  r.timeout_tip='WEB_SYSTEM_TIP' AND r.status = 'UNREAD' AND p.id = :personId "


                + ") t";
        System.out.println(sql);
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        sql = "SELECT COUNT(r.id) FROM car_service.request_timeout_message r  "
                + " INNER JOIN agency_service.persons_cut p ON p.user_id = r.user_id and p.status = 'ENABLED'  "
                + " WHERE r.platform_id = :agencyId AND r.category = 'ORDER' AND p.agency_id = :agencyId AND r.created_dt >= :startDt   AND r.status = 'UNREAD'"
                + " AND (p.id = :personId OR (r.user_id = 1 AND (r.person_ids is null OR position('" + 100004 +"' in r.person_ids) > 0)) AND (r.timeout_tip "
                + "IN ('URGE_ORDER_TIP','AGENCY_SENDBACK_REQ_TIP') OR r.vip_id IS NOT NULL OR r.timeout_tip IN ('PERSON_CANCEL_TIP', 'USER_CANCEL_TIP') AND r.is_auto_second_dispatched IS TRUE ))";
        System.out.println(sql);
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        sql = "SELECT r.id " + " FROM car_service.request_timeout_message r "
                + " INNER JOIN agency_service.persons_cut p ON p.user_id = r.user_id and p.status = 'ENABLED'  "
                + " WHERE r.platform_id = :agencyId and r.category = 'ORDER' and p.agency_id = :agencyId AND r.created_dt >= :startDt "
                + "    AND r.status = 'UNREAD' AND (r.timeout_tip = 'NEW_WEIXIN_TIP' OR r.timeout_tip = 'NEW_OUTER_REQ_TIP' OR r.timeout_tip = 'VI_NEW_TASK_TIP') "
                + "     AND (p.id = :personId " + "       OR ((r.user_id = 1 AND r.person_ids is null) "
                + "            OR (r.user_id = 1 AND r.person_ids is not null AND position('" + 100000000
                + "' in r.person_ids) > 0)) " + "        ) ";
        System.out.println(sql);
        System.out.println("----------------------------------------------------------------------------------------------------------------------");

        sql = "SELECT r.id " + " FROM car_service.request_timeout_message r"
                + " INNER JOIN agency_service.persons_cut p ON p.user_id = r.user_id and p.status = 'ENABLED'"
                + " WHERE r.platform_id = :agencyId and r.category = 'ORDER' and p.agency_id = :agencyId AND r.created_dt >= :startDt "
                + " AND r.status = 'UNREAD' AND (r.timeout_tip = 'URGE_ORDER_TIP' OR r.timeout_tip = 'AGENCY_SENDBACK_REQ_TIP' OR (r.vip_id IS NOT NULL))"
                + " AND (p.id = :personId " + "OR ((r.user_id = 1 AND r.person_ids is null) OR (r.user_id = 1 AND r.person_ids is not null AND position('" + 100000000
                + "' in r.person_ids) > 0)))";
        System.out.println(sql);
        System.out.println("----------------------------------------------------------------------------------------------------------------------");

        StringBuilder sb = new StringBuilder();
        String viptips = "('CREATE_VIP_ORDER_TIP', 'COMPLETED_VIP_ORDER_TIP','CANCELED_VIP_ORDER_TIP', 'URGE_VIP_ORDER_TIP')";
        sb.append("select ")
                .append("msg.id, ")
                .append("case when msg.timeout_tip = 'REQUEST_SUPPLEMENT_TIP' then 'SUPPLEMENT' ")
                .append("	when msg.timeout_tip = 'FINANCE_BALANCE_COMPLETED_TIP' then 'BALANCE_COMPLETED' ")
                .append("	when msg.timeout_tip = 'FINANCE_INVOICED_TIP' then 'INVOICED' ")
                .append("	when msg.timeout_tip = 'FINANCE_PAID_TIP' then 'PAID' ")
                .append("	when msg.timeout_tip = 'REQUEST_TO_AGAIN_APPROVE_TIP' then 'FINANCE_REAUDIT_TIP' ")
                .append("	when msg.timeout_tip = 'REQUEST_FEEDBACK_HANDLE_TIP' then 'FINANCE_FEEDBACK_HANDLE_TIP' ")
                .append("	when msg.timeout_tip = 'FINANCE_BALANCE_ISSUE' then 'BALANCE_ISSUE' ")
                .append("	when msg.timeout_tip = 'FINANCE_BALANCE_TERMINATE' then 'BALANCE_TERMINATE' ")
                .append("	when msg.timeout_tip = 'FINANCE_AUTO_SUBMIT' then 'AUTO_SUBMIT' ")
                .append("	when msg.timeout_tip = 'APP_SYSTEM_TIP' then 'SYSTEM' ")
                .append("	else 'ORDER' ")
                .append("end message_type, ")
                .append("case when msg.timeout_tip = 'REQUEST_SUPPLEMENT_TIP' then '待补充资料订单' ")
                .append("	when msg.timeout_tip = 'FINANCE_BALANCE_COMPLETED_TIP' then '对账完成通知' ")
                .append("	when msg.timeout_tip = 'FINANCE_INVOICED_TIP' then '开票通知' ")
                .append("	when msg.timeout_tip = 'FINANCE_PAID_TIP' then '付款通知' ")
                .append("	when msg.timeout_tip = 'REQUEST_TO_AGAIN_APPROVE_TIP' then '复审通知' ")
                .append("	when msg.timeout_tip = 'REQUEST_FEEDBACK_HANDLE_TIP' then '平台处理订单反馈' ")
                .append("	when msg.timeout_tip = 'FINANCE_BALANCE_ISSUE' then '对账单下发' ")
                .append("	when msg.timeout_tip = 'FINANCE_BALANCE_TERMINATE' then '对账单终止下发' ")
                .append("	when msg.timeout_tip = 'FINANCE_AUTO_SUBMIT' then '对账单自动提交' ")
                .append("	when msg.timeout_tip = 'APPOINTMENT_NOTIFICATION_TIP' and position('未派单' in msg.message) > 0 then '预约单未派单' ")
                .append("	when msg.timeout_tip = 'APPOINTMENT_NOTIFICATION_TIP' and position('未到达' in msg.message) > 0 then '预约单待执行' ")
                .append("	when msg.timeout_tip = 'APP_SYSTEM_TIP' then msg.new_message ")
                .append("	else '订单通知' ")
                .append("end as name, ")
                .append("case when msg.timeout_tip = 'APPOINTMENT_NOTIFICATION_TIP' then msg.message ")
                .append("	when msg.timeout_tip = 'APP_SYSTEM_TIP' then msg.message ")
                .append("	when msg.timeout_tip = 'TASK_HANDLE_TIMEOUT_TIP' then (msg.new_message || '-' || '【' || ct.name || '】' || '-受理订单超时提醒')")
                .append("	when msg.timeout_tip = 'TASK_ACCEPTED_TIMEOUT_TIP' then (msg.new_message || '-' || '【' || ct.name || '】' || '-派单超时提醒')")
                .append("	when msg.timeout_tip = 'TASK_PROCESSING_TIMEOUT_TIP' then (msg.new_message || '-' || '【' || ct.name || '】' || '-到达现场超时提醒')")
                .append("	when msg.timeout_tip in "+ viptips +" then (msg.new_message || '-' || '【' || ct.name || '】' || '-' || msg.message)")
                .append("	else msg.new_message end as content, ")
                .append("msg.request_id order_id, msg.request_code order_no, ")
                .append("case when msg.ext_json is not null then cast(cast(msg.ext_json as json) ->> 'balanceBillId' as bigint) else null end balance_bill_id, ")
                .append("case when msg.ext_json is not null then cast(cast(msg.ext_json as json) ->> 'balanceBillNo' as varchar) else '' end balance_bill_no, ")
                .append("case when msg.ext_json is not null then cast(cast(msg.ext_json as json) ->> 'payBillId' as bigint) else null end pay_bill_id, ")
                .append("case when msg.ext_json is not null then cast(cast(msg.ext_json as json) ->> 'payBillNo' as varchar) else '' end pay_bill_no, ")
                .append("msg.created_dt, msg.status ")
                .append(",msg.forward_url as forwardUrl ")
                .append("from car_service.request_timeout_message msg ")
                .append("left join agency_service.customers c on c.id = msg.vip_id ")
                .append("left join agency_service.customer_types ct on ct.id = c.customer_type_id ")
                .append("where 1=1 ").append(commonMessageCondition4Agency(1L));
        System.out.println(sb.toString());

    }

    public static StringBuilder commonMessageCondition4Agency(Long personId) {
        // vip配置的超时提醒
        String viptips = "('CREATE_VIP_ORDER_TIP', 'COMPLETED_VIP_ORDER_TIP','CANCELED_VIP_ORDER_TIP', 'URGE_VIP_ORDER_TIP','TASK_HANDLE_TIMEOUT_TIP', 'TASK_ACCEPTED_TIMEOUT_TIP', 'TASK_PROCESSING_TIMEOUT_TIP')";
        // 子平台的财务对账消息只显示发送时间是2020年12月1号之后的，生产上。测试环境是5月1号之后
        String financetips = "('FINANCE_BALANCE_COMPLETED_TIP','FINANCE_INVOICED_TIP','FINANCE_PAID_TIP','FINANCE_BALANCE_ISSUE','FINANCE_BALANCE_TERMINATE','FINANCE_AUTO_SUBMIT')";
        StringBuilder consql = new StringBuilder()
                .append("and (msg.platform_id = :platformId  ")
                .append("and (msg.category = 'FINANCE' or ")
                .append("	msg.category = 'ORDER' and msg.timeout_tip in ('NEW_AGENCY_REQ_TIP','REQUEST_SUPPLEMENT_TIP','REQUEST_FEEDBACK_HANDLE_TIP', 'APPOINTMENT_NOTIFICATION_TIP') and (msg.user_id = :userId ")
                .append("		or (msg.user_id = 1 and msg.person_ids is null ")
                .append("			or (msg.user_id = 1 and msg.person_ids is not null and '").append(personId).append("' = any(string_to_array(msg.person_ids, ','))) and msg.timeout_tip = 'NEW_AGENCY_REQ_TIP' ")
                .append("		) ")
                .append("	) ")
                .append(") ")
                // 子平台显示vip消息
                .append(" or (msg.category = 'ORDER' and msg.user_id = 1 and msg.person_ids is not null and '").append(personId).append("' = any(string_to_array(msg.person_ids, ',')) and msg.timeout_tip in "+ viptips +") ")
                //增加系统消息
                .append(" OR (msg.category='SYSTEM' AND msg.person_ids IS NOT NULL AND '"+personId+"' = ANY (string_to_array(msg.person_ids, ',')) AND msg.timeout_tip = 'APP_SYSTEM_TIP') ")
                .append(") ");
        // 2.83.0之后，子平台的财务对账消息只显示发送时间是2020年12月1号之后的，生产上。测试环境是5月1号之后
//        String minDateStr4SubAgencyBalance = Play.configuration.getProperty("ledger.minDateStr4SubAgencyBalance");
//        if (minDateStr4SubAgencyBalance!=null && minDateStr4SubAgencyBalance!="") {
//            consql.append("and (msg.timeout_tip in "+ financetips +" and msg.created_dt >= to_date('"+ minDateStr4SubAgencyBalance +"', 'yyyy-MM-dd') or msg.timeout_tip not in "+ financetips +")");
//        }

        return consql;
    }
}
