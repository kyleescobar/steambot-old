#pragma once
#ifndef STEAMBOT_JVM_LOADER_HPP
#define STEAMBOT_JVM_LOADER_HPP

#include <jni.h>
#include <string>
#include <windows.h>
#include "jnipp.h"

namespace jvmloader {
    void createJVM(const std::string& dir) {
        auto jvmDllPath = dir + std::string(R"(java\jdk-11.0.13+8-jre\bin\client\jvm.dll)");
        auto classPath = dir + std::string("bin\\steambot.jar");

        jni::Vm createJavaVM(jvmDllPath.c_str(), classPath.c_str());

        jni::Class cls = jni::Class("dev/steambot/SteamBot");
        jni::method_t method = cls.getStaticMethod("start", "()V");
        cls.call<void>(method);
    }
}

#endif //STEAMBOT_JVM_LOADER_HPP
