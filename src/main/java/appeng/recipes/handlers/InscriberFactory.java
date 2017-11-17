package appeng.recipes.handlers;

import java.util.Collections;
import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;

import scala.actors.threadpool.Arrays;

import appeng.api.AEApi;
import appeng.api.features.IInscriberRecipeBuilder;
import appeng.api.features.IInscriberRegistry;
import appeng.api.features.InscriberProcessType;
import appeng.recipes.IAERecipeFactory;
import appeng.recipes.factories.recipes.PartRecipeFactory;

public class InscriberFactory implements IAERecipeFactory
{

	@Override
	public void register( JsonObject json, JsonContext ctx )
	{		
		ItemStack result = PartRecipeFactory.getResult( json, ctx );
		String mode  = JsonUtils.getString( json, "mode" );
		List<ItemStack> middle = Arrays.asList( CraftingHelper.getIngredient( JsonUtils.getJsonObject( json, "middle" ), ctx ).getMatchingStacks() );
		
		ItemStack[] top = new ItemStack[] { null };
		if ( json.has( "top" ))
		{
			top = CraftingHelper.getIngredient( JsonUtils.getJsonObject( json, "top" ), ctx ).getMatchingStacks();			
		}

		ItemStack[] bottom = new ItemStack[] { null };
		if ( json.has( "bottom" ))
		{
			bottom = CraftingHelper.getIngredient( JsonUtils.getJsonObject( json, "bottom" ), ctx ).getMatchingStacks();			
		}
		
		
		final IInscriberRegistry reg = AEApi.instance().registries().inscriber();
		for (int i = 0; i < top.length; ++i)
		{
			for (int j = 0; j < bottom.length; ++j)
			{
				final IInscriberRecipeBuilder builder = reg.builder();		
				builder.withOutput( result );
				builder.withProcessType( "press".equals( mode ) ? InscriberProcessType.PRESS : InscriberProcessType.INSCRIBE );
				builder.withInputs( middle );
				
				if ( top[i] != null )
				{
					builder.withTopOptional( top[i] );
				}
				if ( bottom[j] != null )
				{
					builder.withBottomOptional( bottom[j] );
				}
				
				reg.addRecipe( builder.build() );
			}
		}		
	}

}
