FROM ghcr.io/graalvm/graalvm-ce:ol8-java17

ADD build/distributions/scheduler-0.1.tar /
WORKDIR /scheduler-0.1

CMD ["bin/scheduler"]
