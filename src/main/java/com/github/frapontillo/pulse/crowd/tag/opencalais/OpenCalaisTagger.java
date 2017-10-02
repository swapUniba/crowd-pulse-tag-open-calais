package com.github.frapontillo.pulse.crowd.tag.opencalais;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.frapontillo.pulse.crowd.data.entity.Tag;
import com.github.frapontillo.pulse.crowd.tag.ITaggerOperator;
import com.github.frapontillo.pulse.crowd.tag.opencalais.exception
        .OpenCalaisAPILimitReachedException;
import com.github.frapontillo.pulse.crowd.tag.opencalais.exception
        .OpenCalaisUnsupportedLanguageException;
import com.github.frapontillo.pulse.crowd.tag.opencalais.rest.*;
import com.github.frapontillo.pulse.spi.IPlugin;
import com.github.frapontillo.pulse.spi.VoidConfig;
import com.github.frapontillo.pulse.util.PulseLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.Logger;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * Tagging implementation that relies on Thomson Reuters OpenCalais.
 *
 * @author Francesco Pontillo
 * @see
 * {@link "http://developer.permid.org/wp-content/uploads/2015/04/Thomson-Reuters-Open-Calais-API-User-Guide.pdf"}
 */
public class OpenCalaisTagger extends IPlugin<Message, Message, VoidConfig> {
    public final static String PLUGIN_NAME = "opencalais";
    private final static String OPEN_CALAIS_ENDPOINT = "https://api.thomsonreuters.com";
    private final static Logger logger = PulseLogger.getLogger(OpenCalaisTagger.class);

    private OpenCalaisService service;

    @Override public String getName() {
        return PLUGIN_NAME;
    }

    @Override public VoidConfig getNewParameter() {
        return new VoidConfig();
    }

    @Override protected Observable.Operator<Message, Message> getOperator(VoidConfig parameters) {
        return new ITaggerOperator(this) {
            @Override protected List<Tag> getTagsImpl(String text, String language) {
                OpenCalaisResponse response;
                List<Tag> tags = new ArrayList<>();
                boolean retry;

                do {
                    retry = false;
                    try {
                        response = getService().tag(text, getOpenCalaisLanguage(language));
                        for (String entity : response.getEntities()) {
                            Tag tag = new Tag();
                            tag.setText(entity);
                            tag.addSource(getName());
                            tags.add(tag);
                        }
                    } catch (Exception e) {
                        // if the error has a cause and it must be manually handled
                        if (e.getCause() != null) {
                            // unsupported language is OK to flow
                            if (e.getCause().getClass()
                                    .equals(OpenCalaisUnsupportedLanguageException.class)) {
                                // flow through with no warnings
                            }
                            // if the error is related to API limit, retry after a couple of seconds
                            if (e.getCause().getClass()
                                    .equals(OpenCalaisAPILimitReachedException.class)) {
                                logger.info(
                                        "OpenCalais API limit has been reached, waiting for a " +
                                                "couple of seconds...");
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException ignored) {
                                }
                                logger.info("Retrying to call the OpenCalais Web Service...");
                                retry = true;
                            }
                        } else {
                            logger.error(e);
                        }
                    }
                } while (retry);

                // publish the tags as a connectable observable
                return tags;
            }
        };
    }

    private OpenCalaisService getService() {
        if (service == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(OpenCalaisResponse.class, new OpenCalaisResponseAdapter())
                    .create();
            // build the REST client
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(OPEN_CALAIS_ENDPOINT)
                    .setRequestInterceptor(new OpenCalaisInterceptor())
                    .setErrorHandler(new OpenCalaisErrorHandler())
                    .setConverter(new GsonConverter(gson)).build();
            service = restAdapter.create(OpenCalaisService.class);
        }
        return service;
    }

    /**
     * Transform the Crowd Pulse representation of languages into the one accepted by OpenCalais.
     * If the input language isn't supported by OpenCalais (support is for English, Spanish, French
     * only), then a null value will be passed on to Open Calais, which will then determine the
     * language itself.
     *
     * @param crowdPulseLanguage The input language to transform.
     *
     * @return A {@link String} representation of the language as accepted by OpenCalais.
     */
    private String getOpenCalaisLanguage(String crowdPulseLanguage) {
        switch (crowdPulseLanguage) {
            case "en":
                return "English";
            case "es":
                return "Spanish";
            case "fr":
                return "French";
            default:
                return null;
        }
    }
}
