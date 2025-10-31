FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

# --- System dependencies for MATLAB Runtime ---
RUN apt-get update && apt-get install -y --no-install-recommends \
    wget unzip xorg locales xfonts-base libxft2 fontconfig \
    libxext6 libx11-6 libglu1-mesa libxi6 libxtst6 libxrender1 \
    libxrandr2 libxinerama1 libxcursor1 libxcomposite1 libxdamage1 \
    libfontconfig1 libxss1 libasound2 \
    libstdc++6 libc6 \
    python3.10 python3.10-distutils python3-pip \
    && locale-gen en_US.UTF-8 \
    && rm -rf /var/lib/apt/lists/*

ENV LANG=en_US.UTF-8
ENV LC_ALL=en_US.UTF-8

# --- Install Python dependencies ---
COPY requirements.txt /tmp/requirements.txt
RUN pip3 install --no-cache-dir -r /tmp/requirements.txt && rm /tmp/requirements.txt

WORKDIR /opt/mcr

# --- Download & Install MATLAB Runtime silently ---
RUN set -eux; \
    echo ">>> Preparing environment for MATLAB Runtime installation..."; \
    mkdir -p /opt/mcr && chmod 777 /opt/mcr; \
    export HOME=/tmp; \
    echo ">>> Downloading MATLAB Runtime R2025a..."; \
    wget -q https://ssd.mathworks.com/supportfiles/downloads/R2025a/Release/0/deployment_files/installer/complete/glnxa64/MATLAB_Runtime_R2025a_glnxa64.zip; \
    unzip -q MATLAB_Runtime_R2025a_glnxa64.zip -d /tmp/mcr_install; \
    rm -f MATLAB_Runtime_R2025a_glnxa64.zip; \
    echo ">>> Installing MATLAB Runtime..."; \
    HOME=/tmp /tmp/mcr_install/install -mode silent -agreeToLicense yes \
        -destinationFolder /opt/mcr/R2025a -logfile /tmp/mcr_log.txt || { \
        echo "❌ MATLAB Runtime install failed. Log output:"; \
        cat /tmp/mcr_log.txt; \
        exit 1; \
    }; \
    echo ">>> Checking installation result..."; \
    if [ ! -d /opt/mcr/R2025a/bin/glnxa64 ]; then \
        echo "❌ MATLAB Runtime missing expected libraries!"; \
        cat /tmp/mcr_log.txt; \
        exit 1; \
    fi; \
    echo "✅ MATLAB Runtime installed successfully."; \
    rm -rf /tmp/mcr_install /tmp/mcr_log.txt

# --- Set MATLAB Runtime environment paths ---
ENV LD_LIBRARY_PATH=/opt/mcr/R2025a/runtime/glnxa64:/opt/mcr/R2025a/bin/glnxa64:/opt/mcr/R2025a/sys/os/glnxa64:/opt/mcr/R2025a/sys/opengl/lib/glnxa64

# --- Copy compiled MATLAB apps and shell scripts into container ---
WORKDIR /app
COPY ../runSimulationWithAnimation ../run_runSimulationWithAnimation.sh /app/
COPY ../main_run_scenarios ../run_main_run_scenarios.sh /app/
COPY scenarios/ /app/scenarios/
RUN chmod +x runSimulationWithAnimation run_runSimulationWithAnimation.sh main_run_scenarios run_main_run_scenarios.sh

# --- Create a startup script for interactive use ---
RUN echo '#!/bin/bash\n\
echo "MATLAB Energy Saving Simulation Container"\n\
echo "Available commands:"\n\
echo "  ./run_runSimulationWithAnimation.sh /opt/mcr/R2025a scenarios/indoor_hotspot.json"\n\
echo "  ./run_main_run_scenarios.sh /opt/mcr/R2025a"\n\
echo ""\n\
echo "For visualization, run with X11 forwarding:"\n\
echo "docker run -it --rm -e DISPLAY=\$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix <image>"\n\
echo ""\n\
/bin/bash\n\
' > /app/start.sh && chmod +x /app/start.sh

# --- Set environment variables for X11 display ---
ENV DISPLAY=:0.0

# --- Default command ---
CMD ["/app/start.sh"]
