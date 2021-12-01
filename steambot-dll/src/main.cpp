#include <windows.h>
#include <iostream>
#include <ShlObj.h>
#include <string>
#include "util/jvmloader.hpp"
#include "bot/steambot.h"

void __stdcall init() {
    AllocConsole();
    FILE* f;
    freopen_s(&f, "CONOUT$", "w", stdout);
    freopen_s(&f, "CONOUT$", "w", stderr);

    std::cout << "Creating SteamBot JVM inside current process..." << std::endl;

    char path[MAX_PATH];
    SHGetFolderPath(nullptr, CSIDL_PROFILE, nullptr, 0, path);

    std::string classpath = std::string(path) + std::string(R"(\.steambot\)");
    jvmloader::createJVM(classpath);

    steambot::init();
}

BOOL __stdcall DllMain(HMODULE hModule, DWORD dwReason, LPVOID lpReserved) {
    if(dwReason == DLL_PROCESS_ATTACH) {
        CreateThread(nullptr, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(init), hModule, 0, nullptr);
    }
    return TRUE;
}
