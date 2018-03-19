package kz.greetgo.sandbox.db.in_service.model;

public class CheckPoliceResponse {
  public PoliceStatus status;
  public String innWhoChecked;
  public String description;

  @Override
  public String toString() {
    return "CheckPoliceResponse{" +
      "status=" + status +
      ", innWhoChecked='" + innWhoChecked + '\'' +
      ", description='" + description + '\'' +
      '}';
  }
}
