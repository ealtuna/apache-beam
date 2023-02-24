    pipeline
        .apply(Create.of("First"))
        .apply(
            Watch.growthOf(
                    new PollFn<String, String>() {
                      @Override
                      public PollResult<String> apply(String e, Context c) {
                        List<TimestampedValue<String>> outputs = new ArrayList<>();
                        outputs.add(TimestampedValue.of("fff", now()));
                        return PollResult.incomplete(outputs); // can also be complete
                      }
                    })
                .withPollInterval(Duration.standardSeconds(5)))
        .apply(
            ParDo.of(
                new DoFn<KV<String, String>, String>() {
                  @ProcessElement
                  public void processElement(@Element KV<String, String> e, OutputReceiver<String> out) {
                    out.output(e.getValue());
                  }
                }));
