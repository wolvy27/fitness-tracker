package com.fitnesstracker.workouts.businesslayer;




import com.fitnesstracker.workouts.dataaccesslayer.Workout;
import com.fitnesstracker.workouts.dataaccesslayer.WorkoutIdentifier;
import com.fitnesstracker.workouts.dataaccesslayer.WorkoutRepository;
import com.fitnesstracker.workouts.dataaccesslayer.WorkoutType;
import com.fitnesstracker.workouts.datamapperlayer.WorkoutRequestMapper;
import com.fitnesstracker.workouts.datamapperlayer.WorkoutResponseMapper;
import com.fitnesstracker.workouts.presentationlayer.WorkoutRequestModel;
import com.fitnesstracker.workouts.presentationlayer.WorkoutResponseModel;
import com.fitnesstracker.workouts.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private WorkoutRepository workoutRepository;
    private WorkoutResponseMapper workoutResponseMapper;
    private WorkoutRequestMapper workoutRequestMapper;
    //private UserRepository userRepository;
    //private DailyLogRepository dailyLogRepository;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository, WorkoutResponseMapper workoutResponseMapper, WorkoutRequestMapper workoutRequestMapper) {
        this.workoutRepository = workoutRepository;
        this.workoutResponseMapper = workoutResponseMapper;
        this.workoutRequestMapper = workoutRequestMapper;
        //this.userRepository = userRepository;
        //this.dailyLogRepository = dailyLogRepository;
    }
    // Also to push change

    @Override
    public List<WorkoutResponseModel> getWorkouts() {
        /*
        User foundUser = userRepository.findUserByUserIdentifier_UserId(userId);
        if (foundUser == null) {
            throw new NotFoundException("UserId not found: " + userId);
        }

         */
        List<Workout> workouts = workoutRepository.findAll();
        List<Workout> filteredWorkouts = new ArrayList<>();

        for (Workout workout : workouts) {
            if (workout.getWorkoutType() != WorkoutType.NONE) {
                filteredWorkouts.add(workout);
            }
        }
        return workoutResponseMapper.entityToResponseModelList(filteredWorkouts);
    }

    @Override
    public WorkoutResponseModel getWorkoutByWorkoutId(String workoutId) {
        //Workout foundWorkout = workoutRepository.findWorkoutByWorkoutIdentifier_WorkoutIdAndUserIdentifier_UserId(workoutId, userId);
        Workout foundWorkout = workoutRepository.findWorkoutByWorkoutIdentifier_WorkoutId(workoutId);
        if (foundWorkout == null || foundWorkout.getWorkoutType() == WorkoutType.NONE) {
            throw new NotFoundException("WorkoutId not found: " + workoutId);
        }
        /*
        User foundUser = userRepository.findUserByUserIdentifier_UserId(userId);
        if (foundUser == null) {
            throw new NotFoundException("UserId not found: " + userId);
        }

         */

        return workoutResponseMapper.entityToResponseModel(foundWorkout);
    }

    @Override
    public WorkoutResponseModel addWorkout(WorkoutRequestModel newWorkoutData) {
        /*
        User foundUser = userRepository.findUserByUserIdentifier_UserId(userId);
        if (foundUser == null) {
            throw new NotFoundException("UserId not found: " + userId);
        }

         */
        Workout workout = workoutRequestMapper.requestModelToEntity(newWorkoutData);
        workout.setWorkoutIdentifier(new WorkoutIdentifier());
        //workout.setUserIdentifier(foundUser.getUserIdentifier());
        Workout savedWorkout = workoutRepository.save(workout);

        /*
        DailyLog dailyLog = dailyLogRepository.findDailyLogByLogDateAndUserIdentifier_UserId(savedWorkout.getWorkoutDate(), userId);
        if (dailyLog != null) {
            dailyLog.setWorkoutIdentifier(savedWorkout.getWorkoutIdentifier());
            dailyLogRepository.save(dailyLog);
        }
         */
        return workoutResponseMapper.entityToResponseModel(workoutRepository.save(workout));
    }

    @Override
    public WorkoutResponseModel updateWorkout(WorkoutRequestModel newWorkoutData, String workoutId) {
        //Workout foundWorkout = workoutRepository.findWorkoutByWorkoutIdentifier_WorkoutIdAndUserIdentifier_UserId(workoutId, userId);
        Workout foundWorkout = workoutRepository.findWorkoutByWorkoutIdentifier_WorkoutId(workoutId);
        if (foundWorkout == null || foundWorkout.getWorkoutType() == WorkoutType.NONE) {
            throw new NotFoundException("WorkoutId not found: " + workoutId);
        }
        /*
        User foundUser = userRepository.findUserByUserIdentifier_UserId(userId);
        if (foundUser == null) {
            throw new NotFoundException("UserId not found: " + userId);
        }

         */
        Workout updatedWorkout = workoutRequestMapper.requestModelToEntity(newWorkoutData);
        updatedWorkout.setWorkoutIdentifier(foundWorkout.getWorkoutIdentifier());
        //updatedWorkout.setUserIdentifier(foundWorkout.getUserIdentifier());
        updatedWorkout.setId(foundWorkout.getId());

        /*
        DailyLog dailyLog = dailyLogRepository.findDailyLogByLogDateAndUserIdentifier_UserId(updatedWorkout.getWorkoutDate(), userId);
        if (dailyLog != null) {
            dailyLogRepository.save(dailyLog);
        }
         */

        Workout savedWorkout = workoutRepository.save(updatedWorkout);
        return workoutResponseMapper.entityToResponseModel(savedWorkout);
    }

    @Override
    public void deleteWorkout(String workoutId) {
        //Workout foundWorkout = workoutRepository.findWorkoutByWorkoutIdentifier_WorkoutIdAndUserIdentifier_UserId(workoutId, userId);
        Workout foundWorkout = workoutRepository.findWorkoutByWorkoutIdentifier_WorkoutId(workoutId);
        if (foundWorkout == null || foundWorkout.getWorkoutType() == WorkoutType.NONE) {
            throw new NotFoundException("WorkoutId not found: " + workoutId);
        }
        /*
        User foundUser = userRepository.findUserByUserIdentifier_UserId(userId);
        if (foundUser == null) {
            throw new NotFoundException("UserId not found: " + userId);
        }

         */

        /*
        DailyLog dailyLog = dailyLogRepository.findDailyLogByLogDateAndUserIdentifier_UserId(foundWorkout.getWorkoutDate(), userId);
        if (dailyLog != null) {
            if (dailyLog.getWorkoutIdentifier().getWorkoutId().equals(foundWorkout.getWorkoutIdentifier().getWorkoutId())) {
                dailyLog.setWorkoutIdentifier(null);
                dailyLogRepository.save(dailyLog);
            }
        }
         */
        foundWorkout.setWorkoutType(WorkoutType.NONE);

        Workout savedWorkout = workoutRepository.save(foundWorkout);
    }


    /*
    @Override
    public String getWorkoutNameByWorkoutId(String workoutId, String userId) {
        Workout workout = workoutRepository.findWorkoutByWorkoutIdentifier_WorkoutIdAndUserIdentifier_UserId(workoutId, userId);

        if (workout == null) {
            throw new NotFoundException("WorkoutId not found: " + workoutId);
        }
        return workout.getWorkoutName();
    }

    @Override
    public Integer getDurationInMinutesByWorkoutId(String workoutId, String userId) {
        Workout workout = workoutRepository.findWorkoutByWorkoutIdentifier_WorkoutIdAndUserIdentifier_UserId(workoutId, userId);

        if (workout == null) {
            throw new NotFoundException("WorkoutId not found: " + workoutId);
        }
        return workout.getDurationInMinutes();
    }

     */


}
