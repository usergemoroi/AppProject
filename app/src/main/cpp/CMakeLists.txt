cmake_minimum_required(VERSION 3.22.1)

project("bombaday")

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O3 -Wall")

add_library(bombaday SHARED
        video_compressor.cpp
        native-lib.cpp)

find_library(log-lib log)
find_library(android-lib android)
find_library(jnigraphics-lib jnigraphics)

target_link_libraries(bombaday
        ${log-lib}
        ${android-lib}
        ${jnigraphics-lib})
