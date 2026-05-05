package com.disasteralert.userservice.infrastructure.repository;

import com.disasteralert.userservice.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    // Deprem/hava uyarısı açık olan kullanıcılar
    List<User> findByEarthquakeAlertsEnabledTrue();
    List<User> findByWeatherAlertsEnabledTrue();

    // Belirli şehirdeki kullanıcılar
    List<User> findByCity(String city);

    // Konum bazlı — belirli yarıçap içindeki kullanıcılar
    // Haversine formülü — iki koordinat arası mesafe
    @Query("""
        SELECT u FROM User u
        WHERE (6371 * acos(
            cos(radians(:lat)) * cos(radians(u.latitude)) *
            cos(radians(u.longitude) - radians(:lon)) +
            sin(radians(:lat)) * sin(radians(u.latitude))
        )) < :radiusKm
        AND u.earthquakeAlertsEnabled = true
        """)
    List<User> findUsersInRadius(
            @Param("lat") Double latitude,
            @Param("lon") Double longitude,
            @Param("radiusKm") Double radiusKm
    );
}