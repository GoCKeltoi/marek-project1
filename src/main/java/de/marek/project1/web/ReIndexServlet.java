package de.marek.project1.web;


import javax.servlet.http.HttpServlet;


public class ReIndexServlet extends HttpServlet {

//    private final FullIndexBuilder fullIndexBuilder;
//    private final MetricRegistry mr;
//    private final DateTimeFormatter timeFormatter;
//
//    public ReIndexServlet(FullIndexBuilder fullIndexBuilder, MetricRegistry mr) {
//        this.fullIndexBuilder = fullIndexBuilder;
//        this.mr = mr;
//        this.timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
//        mr.meter(name("inbound", "fullindex")).mark();
//        String ts = timeFormatter.format(LocalDateTime.now(Clock.systemUTC())) + "utc";
//
//        final Thread indexer = new Thread(() -> fullIndexBuilder.fullIndex(), "fullindex-" + ts);
//        indexer.start();
//
//        if (req.getParameter("synchronous") != null) {
//            try {
//                indexer.join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
//    }
}
