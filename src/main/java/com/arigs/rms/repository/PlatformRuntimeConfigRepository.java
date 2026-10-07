package com.arigs.rms.repository;

import com.arigs.rms.entity.PlatformRuntimeConfig;
import java.util.List;

public interface PlatformRuntimeConfigRepository extends BaseRepository<PlatformRuntimeConfig> {

    List<PlatformRuntimeConfig> findByActiveTrueAndDeletedFalseOrderByConfigKeyAsc();
}
