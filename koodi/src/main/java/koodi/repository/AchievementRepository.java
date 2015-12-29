package koodi.repository;

import koodi.domain.Achievement;

public interface AchievementRepository extends BaseRepository<Achievement> {

    public Achievement findByName(String achievementName);
    
}
