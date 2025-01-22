public class Request extends GeneralObject {
    // Request Variables
    private String reqUID;
    private String patUID;
    private String servCode;
    private String testType;
    private long reqDate;
    private long reqTime;
    private String result;

    public Request(
        String reqUID,
        String patUID,
        String servCode,
        String testType,
        long reqDate,
        long reqTime,
        String result,
        char delIndicator,
        String delReason
    ) {
        super(delIndicator, delReason);
        this.reqUID = reqUID;
        this.patUID = patUID;
        this.servCode = servCode;
        this.testType = testType;
        this.reqDate = reqDate;
        this.reqTime = reqTime;
        this.result = result;
    }

    public String getReqUID() {
        return reqUID;
    }

    public void setReqUID(String reqUID) {
        this.reqUID = reqUID;
    }

    public String getPatUID() {
        return patUID;
    }

    public void setPatUID(String patUID) {
        this.patUID = patUID;
    }

    public String getServCode() {
        return servCode;
    }

    public void setServCode(String servCode) {
        this.servCode = servCode;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public long getReqDate() {
        return reqDate;
    }

    public void setReqDate(long reqDate) {
        this.reqDate = reqDate;
    }

    public long getReqTime() {
        return reqTime;
    }

    public void setReqTime(long reqTime) {
        this.reqTime = reqTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }   
    
}
