# ==========================================
# MATLAB Energy Saving Simulation Container
# Build verified for MATLAB Runtime R2025a
# ==========================================

FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

# --- System dependencies (MATLAB + Python) ---
RUN apt-get update && apt-get install -y --no-install-recommends \
    wget unzip xorg locales xfonts-base libxft2 fontconfig libfreetype6 \
    libxrender1 libxt6 libxi6 libxrandr2 libglu1-mesa \
    libgomp1 libgcc-s1 libexpat1 \
    libxext6 libx11-6 libxi6 libxtst6 libxrender1 \
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

# --- Prepare directory for MATLAB Runtime ---
WORKDIR /opt/mcr

# --- Download & Install MATLAB Runtime silently ---
RUN set -eux; \
    echo ">>> Preparing environment for MATLAB Runtime installation..."; \
    mkdir -p /opt/mcr /root/.Xauthority /tmp/.X11-unix; \
    chmod -R 777 /opt/mcr /tmp /root; \
    export HOME=/tmp LANG=en_US.UTF-8; \
    echo ">>> Downloading MATLAB Runtime R2025a (this may take several minutes)..."; \
    wget -q https://ssd.mathworks.com/supportfiles/downloads/R2025a/Release/0/deployment_files/installer/complete/glnxa64/MATLAB_Runtime_R2025a_glnxa64.zip; \
    unzip -q MATLAB_Runtime_R2025a_glnxa64.zip -d /tmp/mcr_install; \
    rm -f MATLAB_Runtime_R2025a_glnxa64.zip; \
    echo ">>> Installing MATLAB Runtime (silent mode)..."; \
    HOME=/tmp /tmp/mcr_install/install -mode silent -agreeToLicense yes \
        -destinationFolder /opt/mcr/R2025a -logfile /tmp/mcr_log.txt || true; \
    echo ">>> INSTALL LOG (last 20 lines):"; \
    tail -n 20 /tmp/mcr_log.txt || true; \
    echo ">>> Checking installation result..."; \
    if [ ! -d /opt/mcr/R2025a/bin/glnxa64 ]; then \
        echo "❌ MATLAB Runtime installation failed. Full log below:"; \
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
