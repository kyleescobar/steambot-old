cmake_minimum_required(VERSION 3.20)
project(steambot)

set(CMAKE_C_FLAGS_RELEASE "-O3 -fvisibility=hidden -flto -fno-exceptions -DNDEBUG -Wfatal-errors")

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY build/)

if (MSVC)
    add_compile_options(
            $<$<CONFIG:>:/MT> #---------|
            $<$<CONFIG:Debug>:/MTd> #---|-- Statically link the runtime libraries
            $<$<CONFIG:Release>:/MT> #--|
    )
    add_compile_options(/EHa) # enable C++ EH (w/ SEH exceptions)
    add_compile_options(/Zi) # enable debugging information
endif ()

set(VCPKG_TARGET_TRIPLET "x64-windows-static" CACHE STRING "" FORCE)

file(GLOB_RECURSE SOURCES ${CMAKE_BUILD_DIR} "src/*.cpp" "src/*.h" "src/*.hpp")

find_package(JNI REQUIRED)

include_directories(include/ ${JNI_INCLUDE_DIRS})
link_directories(lib/ ${JNI_LIBRARIES})

add_library(steambot SHARED ${SOURCES})

target_include_directories(steambot PRIVATE include/ ${JNI_INCLUDE_DIRS})
target_link_libraries(steambot PRIVATE PolyHook_2 ${JNI_LIBRARIES})
