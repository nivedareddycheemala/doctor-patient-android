package Models;

public class Appointmentdata {

    private String patientname, doctorname, appointmentdate,doctornumber,patientnumber,apptstatus;

    public Appointmentdata(String patientname, String doctorname, String appointmentdate,String doctornumber,String patientnumber,String apptstatus) {
        this.patientname = patientname;
        this.doctorname = doctorname;
        this.appointmentdate = appointmentdate;
        this.doctornumber=doctornumber;
        this.patientnumber=patientnumber;
        this.apptstatus=apptstatus;
    }

    public Appointmentdata() {
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getAppointmentdate() {
        return appointmentdate;
    }

    public void setAppointmentdate(String appointmentdate) {
        this.appointmentdate = appointmentdate;
    }

    public String getDoctornumber() {
        return doctornumber;
    }

    public void setDoctornumber(String doctornumber) {
        this.doctornumber = doctornumber;
    }

    public String getPatientnumber() {
        return patientnumber;
    }

    public void setPatientnumber(String patientnumber) {
        this.patientnumber = patientnumber;
    }

    public String getApptstatus() {
        return apptstatus;
    }

    public void setApptstatus(String apptstatus) {
        this.apptstatus = apptstatus;
    }
}
