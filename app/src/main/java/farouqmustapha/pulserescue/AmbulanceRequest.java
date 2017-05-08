package farouqmustapha.pulserescue;

/**
 * Created by Farouq Mustapha on 5/5/2017.
 */

public class AmbulanceRequest {
    private String time;
    private String patientKey;
    private String requestStatus;
    private String patientLatitude;
    private String patientLongitude;
    private String key;
    private String name;

    public AmbulanceRequest(){

    }

    public AmbulanceRequest(String time, String patientKey, String requestStatus, String patientLatitude, String patientLongitude, String name){
        this.time = time;
        this.patientKey = patientKey;
        this.requestStatus = requestStatus;
        this.patientLatitude = patientLatitude;
        this.patientLongitude = patientLongitude;
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getPatientLatitude() {
        return patientLatitude;
    }

    public void setPatientLatitude(String patientLatitude) {
        this.patientLatitude = patientLatitude;
    }

    public String getPatientLongitude() {
        return patientLongitude;
    }

    public void setPatientLongitude(String patientLongitude) {
        this.patientLongitude = patientLongitude;
    }

    public String getPatientKey() {
        return patientKey;
    }

    public void setPatientKey(String patientKey) {
        this.patientKey = patientKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
