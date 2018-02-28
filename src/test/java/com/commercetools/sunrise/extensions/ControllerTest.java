package com.commercetools.sunrise.extensions;

import org.apache.commons.io.IOUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import static com.commercetools.sunrise.extensions.ExtensionHeaders.AUTH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

@RunWith(SpringRunner.class)
@WebMvcTest(ExtensionsController.class)
@ActiveProfiles({"test"})
@ContextConfiguration(classes = DefaultWiring.class)
public abstract class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    protected ResultActions testUrlWithInput(final String url, final String inputFilepath) throws Exception {
        final String content = IOUtils.toString(new ClassPathResource(inputFilepath).getInputStream());
        final RequestBuilder request = post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)
                .header(AUTH, "secret");
        final MvcResult mvcResult = this.mockMvc.perform(request)
                .andExpect(request().asyncStarted())
                .andReturn();
        return this.mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print());
    }
}
