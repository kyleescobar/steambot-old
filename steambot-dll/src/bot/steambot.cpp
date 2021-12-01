#include "steambot.h"

PLH::CapstoneDisassembler* disasm = nullptr;

namespace steambot {
    namespace module {
        void load() {
            base = reinterpret_cast<uint64_t>(GetModuleHandleA("osclient.exe"));
        }
    }
    
    namespace globals {
        int* viewportTempX = nullptr;
        int* viewportTempY = nullptr;

        void load() {
            viewportTempX = reinterpret_cast<int*>(module::base + 0x531d08);
            viewportTempY = reinterpret_cast<int*>(module::base + 0x531d0c);
        }
    }

    namespace functions {
        worldToScreen_t worldToScreen = nullptr;
        
        void load() {
            worldToScreen = (worldToScreen_t)(module::base + 0x84540);
        }
    }

    namespace hooks {
        void load() {
            disasm = new PLH::CapstoneDisassembler(PLH::Mode::x64);
            UnhookWindowsHookEx(*((HHOOK*)(module::base + 0x1814188)));
        }
    }

    void init() {
        module::load();
        functions::load();
        globals::load();
        hooks::load();
        std::cout << "Successfully initialized SteamBot hooks." << std::endl;
    }
}

extern "C" __declspec(dllexport) void worldToScreen(int& screenX, int& screenY, int x, int y, int height) {
    steambot::functions::worldToScreen(nullptr, nullptr, x, y, height);
    screenX = *reinterpret_cast<int*>(steambot::globals::viewportTempX);
    screenY = *reinterpret_cast<int*>(steambot::globals::viewportTempY);
}