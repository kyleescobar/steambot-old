#include "steambot.h"

PLH::CapstoneDisassembler* disasm = nullptr;

uint64_t tramp_doCycle = NULL;
uint64_t tramp_doCycleLoggedOut = NULL;

PLH::x64Detour* detour_doCycle = nullptr;
PLH::x64Detour* detour_doCycleLoggedOut = nullptr;

void* hook_doCycle(void* p1, uint32_t p2, uint32_t p3, uint32_t p4, long p5, uint32_t p6);
void hook_doCycleLoggedOut(int* p1, uintptr_t p2, uintptr_t p3, uintptr_t p4, uintptr_t p5);

namespace steambot {
    namespace module {
        void load() {
            base = reinterpret_cast<uint64_t>(GetModuleHandleA("osclient.exe"));
        }
    }

    namespace functions {
        doCycle_t doCycle = nullptr;
        doCycleLoggedOut_t doCycleLoggedOut = nullptr;

        void load() {
            doCycle = (doCycle_t)(module::base + 0x68120);
            doCycleLoggedOut = (doCycleLoggedOut_t)(module::base + 0x8e9b0);
        }
    }

    namespace hooks {
        void load() {
            disasm = new PLH::CapstoneDisassembler(PLH::Mode::x64);

            detour_doCycle = new PLH::x64Detour(
                        reinterpret_cast<uint64_t>(steambot::functions::doCycle),
                        reinterpret_cast<uint64_t>(&hook_doCycle),
                        &tramp_doCycle,
                        *disasm
                    );
            detour_doCycle->hook();

            detour_doCycleLoggedOut = new PLH::x64Detour(
                        reinterpret_cast<uint64_t>(steambot::functions::doCycleLoggedOut),
                        reinterpret_cast<uint64_t>(&hook_doCycleLoggedOut),
                        &tramp_doCycleLoggedOut,
                        *disasm
                    );
            detour_doCycleLoggedOut->hook();
        }
    }

    void init() {
        module::load();
        functions::load();
        hooks::load();
        std::cout << "Successfully initialized SteamBot hooks." << std::endl;
    }
}

void* hook_doCycle(void* p1, uint32_t p2, uint32_t p3, uint32_t p4, long p5, uint32_t p6) {
    std::cout << "Hooked!" << std::endl;
    return PLH::FnCast(tramp_doCycle, steambot::functions::doCycle_t())(p1, p2, p3, p4, p5, p6);
}

void hook_doCycleLoggedOut(int* p1, uintptr_t p2, uintptr_t p3, uintptr_t p4, uintptr_t p5) {
    std::cout << "Boom!" << std::endl;
    PLH::FnCast(tramp_doCycle, steambot::functions::doCycleLoggedOut_t())(p1, p2, p3, p4, p5);
}