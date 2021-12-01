#include "steambot.h"

PLH::CapstoneDisassembler* disasm = nullptr;

namespace steambot {
    namespace module {
        void load() {
            base = reinterpret_cast<uint64_t>(GetModuleHandleA("osclient.exe"));
        }
    }

    namespace functions {
        void load() {

        }
    }

    namespace hooks {
        void load() {
            disasm = new PLH::CapstoneDisassembler(PLH::Mode::x64);
        }
    }

    void init() {
        module::load();
        functions::load();
        hooks::load();
        std::cout << "Successfully initialized SteamBot hooks." << std::endl;
    }
}