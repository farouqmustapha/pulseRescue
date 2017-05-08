package farouqmustapha.pulserescue;

/**
 * Created by Farouq Mustapha on 5/8/2017.
 */

public class PersonalInfo {
    private String address;
    private String age;
    private String bloodType;
    private String emergencyNumber;
    private String emergencyPerson;
    private String height;
    private String name;
    private String phone;
    private String weight;

    public PersonalInfo(){

    }

    public PersonalInfo(String address,String age, String bloodType, String emergencyNumber, String emergencyPerson,
                        String height, String name, String phone, String weight){
        this.setAddress(address);
        this.setAge(age);
        this.setBloodType(bloodType);
        this.setEmergencyNumber(emergencyNumber);
        this.setEmergencyPerson(emergencyPerson);
        this.setHeight(height);
        this.setName(name);
        this.setPhone(phone);
        this.setWeight(weight);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public String getEmergencyPerson() {
        return emergencyPerson;
    }

    public void setEmergencyPerson(String emergencyPerson) {
        this.emergencyPerson = emergencyPerson;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
