package com.itdom;



public class Sample {
    public static void main(String[] args) {

        String sql = "select req.*, vi.* from car_service.service_reqs req "
                + " LEFT JOIN car_service.vi_requests vi ON req.id = vi.id "
                + " where (req.is_deleted = false OR req.is_deleted is null)  and req.id in (" + "requestIdStr" + ")";


        System.out.println(sql);

    }
}
