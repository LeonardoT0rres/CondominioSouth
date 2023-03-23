package com.easyvoteapi.utils.constants;

import com.easyvoteapi.utils.mappers.AssemblyMapper;
import com.easyvoteapi.utils.mappers.ScheduleMapper;
import com.easyvoteapi.utils.mappers.UserMapper;
import com.easyvoteapi.utils.mappers.VoteMapper;
import org.mapstruct.factory.Mappers;

public class MapperConstants {
    public static final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    public static final ScheduleMapper scheduleMapper = Mappers.getMapper(ScheduleMapper.class);
    public static final AssemblyMapper assemblyMapper = Mappers.getMapper(AssemblyMapper.class);
    public static final VoteMapper voteMapper = Mappers.getMapper(VoteMapper.class);
}
