# ==========================================
# MATLAB Energy Saving Simulation Container
# Based on official MATLAB Runtime R2025a image
# ==========================================

FROM containers.mathworks.com/matlab-runtime:r2025a

LABEL maintainer="donuva2" \
      description="Energy Saving 5G Simulation with MATLAB Runtime R2025a" \
      version="1.0"

# --- System update & Python environment ---
RUN apt-get update && apt-get install -y --no-install-recommends \
    python3.10 python3.10-distutils python3-pip \
    && rm -rf /var/lib/apt/lists/*

# --- Install Python dependencies (if needed for pre/post processing) ---
COPY requirements.txt /tmp/requirements.txt
RUN pip3 install --no-cache-dir -r /tmp/requirements.txt && rm /tmp/requirements.txt

# --- Set working directory ---
WORKDIR /app

# --- Copy MATLAB compiled scripts and scenarios ---
COPY runSimulationWithAnimation run_runSimulationWithAnimation.sh /app/
COPY main_run_scenarios run_main_run_scenarios.sh /app/
COPY scenarios/ /app/scenarios/

# --- Give execution permission ---
RUN chmod +x runSimulationWithAnimation run_runSimulationWithAnimation.sh main_run_scenarios run_main_run_scenarios.sh

# --- Environment setup ---
ENV DISPLAY=:0.0
ENV LD_LIBRARY_PATH=/opt/matlab/R2025a/runtime/glnxa64:/opt/matlab/R2025a/bin/glnxa64:/opt/matlab/R2025a/sys/os/glnxa64:/opt/matlab/R2025a/sys/opengl/lib/glnxa64

# --- Optional: create startup script for interactive use ---
RUN echo '#!/bin/bash\n\
echo "============================================="\n\
echo " MATLAB Energy Saving 5G Simulation Container"\n\
echo "============================================="\n\
echo ""\n\
echo "Available commands:"\n\
echo "  ./run_runSimulationWithAnimation.sh /opt/matlab/R2025a scenarios/indoor_hotspot.json"\n\
echo "  ./run_main_run_scenarios.sh /opt/matlab/R2025a"\n\
echo ""\n\
echo "To visualize (X11):"\n\
echo "  docker run -it --rm -e DISPLAY=\$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix <image>"\n\
echo ""\n\
/bin/bash\n\
' > /app/start.sh && chmod +x /app/start.sh

# --- Default command ---
CMD ["/app/start.sh"]
