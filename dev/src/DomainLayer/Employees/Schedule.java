package DomainLayer.Employees;
import DAL.ShiftDAO;
import ServiceLayer.Utility.ShiftPair;
import ServiceLayer.Utility.ShiftDate;

import java.util.List;

public class Schedule {
    private List<ShiftPair> schedule;
    
    public Schedule(List<ShiftPair> schedule){
        this.schedule = schedule;
    }

    public List<ShiftPair> getSchedule() {
        return schedule;
    }

    /**
     * @param date date to be checked
     * @param timeOfDay time of the day to be checked
     * @return true if the holder of the schedule is free in the given time frame
     */
    public boolean getAvailabilityStatus(ShiftDate date, Time timeOfDay) {
        for (ShiftPair p : schedule){
            if(p.getDate().equals(date) && p.getTime().compareTo(timeOfDay) == 0)
                return true;
        }
        return false;
    }

    public void addTimeSlot(ShiftPair shiftTime){
        for (ShiftPair p : schedule){
            if (p.equals(shiftTime))
                return;
        }
        schedule.add(shiftTime);
    }

    public void removeTimeSlot(ShiftPair shiftTime){
        for(ShiftPair p: schedule){
            if(p.equals(shiftTime)){
                schedule.remove(p);
            }
            return;
        }
    }

    @Override
    public String toString() {
        if (schedule.size() == 0) {
            return "Unspecified";
        }
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < schedule.size(); i++){
            ret.append(schedule.get(i).toString());
            if (i != schedule.size()-1)
                ret.append(", ");
        }
        return ret.toString();
    }
}
